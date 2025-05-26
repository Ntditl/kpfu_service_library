package com.library.repositories

import com.library.database.Borrowings
import com.library.models.BorrowingDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class BorrowingRepository {

    fun getAll(): List<BorrowingDTO> = transaction {
        Borrowings.selectAll().map { toBorrowingDTO(it) }
    }

    fun getById(id: Int): BorrowingDTO? = transaction {
        Borrowings.select { Borrowings.reservationId eq id }
            .map { toBorrowingDTO(it) }
            .singleOrNull()
    }

    fun create(borrowing: BorrowingDTO): Int = transaction {
        Borrowings.insert {
            it[bookCopyId] = borrowing.book_copy_id
            it[userId] = borrowing.user_id
            it[dateRequestFromUser] = borrowing.date_request_from_user
            borrowing.date_answer_to_request?.let { date -> it[dateAnswerToRequest] = date }
            borrowing.res_answer_to_user?.let { answer -> it[resAnswerToUser] = answer }
            borrowing.date_of_start_of_issuance?.let { date -> it[dateOfStartOfIssuance] = date }
            borrowing.date_to_return?.let { date -> it[dateToReturn] = date }
        } get Borrowings.reservationId
    }

    fun update(id: Int, borrowing: BorrowingDTO): Boolean = transaction {
        Borrowings.update({ Borrowings.reservationId eq id }) {
            it[bookCopyId] = borrowing.book_copy_id
            it[userId] = borrowing.user_id
            it[dateRequestFromUser] = borrowing.date_request_from_user
            borrowing.date_answer_to_request?.let { date -> it[dateAnswerToRequest] = date }
            borrowing.res_answer_to_user?.let { answer -> it[resAnswerToUser] = answer }
            borrowing.date_of_start_of_issuance?.let { date -> it[dateOfStartOfIssuance] = date }
            borrowing.date_to_return?.let { date -> it[dateToReturn] = date }
        } > 0
    }

    fun delete(id: Int): Boolean = transaction {
        Borrowings.deleteWhere { Borrowings.reservationId eq id } > 0
    }

    private fun toBorrowingDTO(row: ResultRow): BorrowingDTO {
        return BorrowingDTO(
            reservation_id = row[Borrowings.reservationId],
            book_copy_id = row[Borrowings.bookCopyId],
            user_id = row[Borrowings.userId],
            date_request_from_user = row[Borrowings.dateRequestFromUser],
            date_answer_to_request = row[Borrowings.dateAnswerToRequest],
            res_answer_to_user = row[Borrowings.resAnswerToUser],
            date_of_start_of_issuance = row[Borrowings.dateOfStartOfIssuance],
            date_to_return = row[Borrowings.dateToReturn]
        )
    }
} 