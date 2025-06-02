package com.library.services

import com.library.models.BorrowingDTO
import com.library.models.UpdateBorrowingDTO
import com.library.models.UpdateBorrowingReturnDateDTO
import com.library.repositories.BorrowingRepository
import com.library.repositories.BookCopyRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.select
import com.library.database.BookCopies
import com.library.database.Borrowings
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.JoinType

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

        return transaction {
            // Проверяем, нет ли у пользователя уже активной заявки на эту книгу
            val existingBorrowing = Borrowings
                .join(BookCopies, JoinType.INNER, onColumn = Borrowings.bookCopyId, otherColumn = BookCopies.copyId)
                .select { 
                    (BookCopies.bookId eq bookId) and 
                    (Borrowings.userId eq userId) and
                    (Borrowings.dateAnswerToRequest.isNull())
                }
                .firstOrNull()

            if (existingBorrowing != null) {
                throw IllegalStateException("You already have an active request for this book")
            }

            // Находим доступную копию книги
            val availableCopy = BookCopies.select { 
                (BookCopies.bookId eq bookId) and 
                (BookCopies.isInHere eq true) and 
                (BookCopies.isInReservation eq false) and
                (BookCopies.isInBorrowedByUser eq false)
            }.firstOrNull() ?: throw IllegalStateException("No available copies of this book")

            val copyId = availableCopy[BookCopies.copyId]

            // Помечаем копию как запрошенную пользователем
            BookCopies.update({ BookCopies.copyId eq copyId }) { stmt ->
                stmt[BookCopies.isInBorrowedByUser] = true
            }

            try {
                // Создаем заявку на выдачу
                val dto = BorrowingDTO(
                    book_copy_id = copyId,
                    user_id = userId,
                    date_request_from_user = dateRequestFromUser
                )
                repository.create(dto)
            } catch (e: Exception) {
                // В случае ошибки освобождаем пометку
                BookCopies.update({ BookCopies.copyId eq copyId }) { stmt ->
                    stmt[BookCopies.isInBorrowedByUser] = false
                }
                throw e
            }
        }
    }

    fun update(id: Int, dto: UpdateBorrowingDTO): Boolean {
        require(id > 0) { "ID must be positive" }
        // Проверки на положительность ID в DTO убраны, так как они nullable
        
        return transaction {
            val borrowing = repository.getById(id) ?: return@transaction false

            // Если заявка отклонена или завершена, снимаем пометку
            // Считаем завершенной, если есть дата ответа и дата возврата
            if (dto.res_answer_to_user == false || (dto.date_answer_to_request != null && dto.date_to_return != null)) {
                 BookCopies.update({ BookCopies.copyId eq borrowing.book_copy_id }) { stmt ->
                     stmt[BookCopies.isInBorrowedByUser] = false
                 }
            }

            repository.update(id, dto)
        }
    }

    fun delete(id: Int): Boolean {
        require(id > 0) { "ID must be positive" }
        
        return transaction {
            val borrowing = repository.getById(id) ?: return@transaction false
            
            // При удалении заявки снимаем пометку
            BookCopies.update({ BookCopies.copyId eq borrowing.book_copy_id }) { stmt ->
                stmt[BookCopies.isInBorrowedByUser] = false
            }
            
            repository.delete(id)
        }
    }

    fun updateReturnDate(id: Int, dto: UpdateBorrowingReturnDateDTO): Boolean {
        require(id > 0) { "ID must be positive" }
        return repository.updateReturnDate(id, dto.date_to_return)
    }
} 