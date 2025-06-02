package com.library.repositories

import com.library.database.BookCopies
import com.library.models.BookCopyDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class BookCopyRepository {

    fun getAll(): List<BookCopyDTO> = transaction {
        BookCopies.selectAll().map {
            BookCopyDTO(
                copy_id = it[BookCopies.copyId],
                book_id = it[BookCopies.bookId],
                is_in_here = it[BookCopies.isInHere],
                is_in_reservation = it[BookCopies.isInReservation],
                borrowed_By_Other_User = it[BookCopies.isInBorrowedByUser]
            )
        }
    }

    fun getById(id: Int): BookCopyDTO? = transaction {
        BookCopies.select { BookCopies.copyId eq id }
            .map {
                BookCopyDTO(
                    copy_id = it[BookCopies.copyId],
                    book_id = it[BookCopies.bookId],
                    is_in_here = it[BookCopies.isInHere],
                    is_in_reservation = it[BookCopies.isInReservation],
                    borrowed_By_Other_User = it[BookCopies.isInBorrowedByUser]
                )
            }
            .singleOrNull()
    }

    fun create(bookCopy: BookCopyDTO): Int = transaction {
        BookCopies.insert {
            it[bookId] = bookCopy.book_id
            it[isInHere] = bookCopy.is_in_here
            it[isInReservation] = bookCopy.is_in_reservation
            it[BookCopies.isInBorrowedByUser] = bookCopy.borrowed_By_Other_User ?: false
        } get BookCopies.copyId
    }

    fun update(id: Int, bookCopy: BookCopyDTO): Boolean = transaction {
        BookCopies.update({ BookCopies.copyId eq id }) {
            it[bookId] = bookCopy.book_id
            it[isInHere] = bookCopy.is_in_here
            it[isInReservation] = bookCopy.is_in_reservation
            bookCopy.borrowed_By_Other_User?.let { borrowedStatus -> it[BookCopies.isInBorrowedByUser] = borrowedStatus }
        } > 0
    }

    fun delete(id: Int): Boolean = transaction {
        BookCopies.deleteWhere { BookCopies.copyId eq id } > 0
    }

    fun updateReservation(id: Int, isInReservation: Boolean): Boolean = transaction {
        BookCopies.update({ BookCopies.copyId eq id }) { stmt ->
            stmt[BookCopies.isInReservation] = isInReservation
        } > 0
    }

    fun updateLocation(id: Int, isInHere: Boolean): Boolean = transaction {
        BookCopies.update({ BookCopies.copyId eq id }) { stmt ->
            stmt[BookCopies.isInHere] = isInHere
        } > 0
    }

    fun updateBorrowedStatus(id: Int, borrowedByOtherUser: Boolean): Boolean = transaction {
        BookCopies.update({ BookCopies.copyId eq id }) { stmt ->
            stmt[BookCopies.isInBorrowedByUser] = borrowedByOtherUser
        } > 0
    }
} 