package com.library.services

import com.library.models.BorrowingDTO
import com.library.models.UpdateBorrowingDTO
import com.library.repositories.BorrowingRepository
import com.library.repositories.BookCopyRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.select
import com.library.database.BookCopies
import com.library.database.Borrowings
import org.jetbrains.exposed.sql.and

class BorrowingService(
    private val repository: BorrowingRepository = BorrowingRepository(),
    private val bookCopyRepository: BookCopyRepository = BookCopyRepository()
) {

    fun getAll(): List<BorrowingDTO> {
        return repository.getAll()
    }

    fun getById(id: Int): BorrowingDTO? {
        require(id > 0) { "ID must be positive" }
        return repository.getById(id)
    }

    fun create(dto: BorrowingDTO): Int {
        require(dto.book_copy_id > 0) { "Book copy ID must be positive" }
        require(dto.user_id > 0) { "User ID must be positive" }
        return repository.create(dto)
    }

    fun createByBookId(bookId: Int, userId: Int, dateRequestFromUser: java.time.LocalDateTime): Int {
        require(bookId > 0) { "Book ID must be positive" }
        require(userId > 0) { "User ID must be positive" }

        // Находим доступную копию книги
        val availableCopy = transaction {
            BookCopies.select { 
                (BookCopies.bookId eq bookId) and 
                (BookCopies.isInHere eq true) and 
                (BookCopies.isInReservation eq false)
            }.firstOrNull()
        } ?: throw IllegalStateException("No available copies of this book")

        val copyId = availableCopy[BookCopies.copyId]

        // Создаем заявку на выдачу
        val dto = BorrowingDTO(
            book_copy_id = copyId,
            user_id = userId,
            date_request_from_user = dateRequestFromUser
        )

        return repository.create(dto)
    }

    fun update(id: Int, dto: UpdateBorrowingDTO): Boolean {
        require(id > 0) { "ID must be positive" }
        dto.book_copy_id?.let { require(it > 0) { "Book copy ID must be positive" } }
        dto.user_id?.let { require(it > 0) { "User ID must be positive" } }
        return repository.update(id, dto)
    }

    fun delete(id: Int): Boolean {
        require(id > 0) { "ID must be positive" }
        return repository.delete(id)
    }
} 