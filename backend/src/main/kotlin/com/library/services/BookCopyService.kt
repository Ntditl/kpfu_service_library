package com.library.services

import com.library.models.BookCopyDTO
import com.library.models.UpdateBookCopyReservationDTO
import com.library.models.UpdateBookCopyLocationDTO
import com.library.models.UpdateBookCopyBorrowedStatusDTO
import com.library.repositories.BookCopyRepository

class BookCopyService(
    private val repository: BookCopyRepository = BookCopyRepository()
) {

    fun getAll(): List<BookCopyDTO> {
        return repository.getAll()
    }

    fun getById(id: Int): BookCopyDTO? {
        require(id > 0) { "ID must be positive" }
        return repository.getById(id)
    }

    fun create(dto: BookCopyDTO): Int {
        require(dto.book_id > 0) { "Book ID must be positive" }
        return repository.create(dto)
    }

    fun update(id: Int, dto: BookCopyDTO): Boolean {
        require(id > 0) { "ID must be positive" }
        require(dto.book_id > 0) { "Book ID must be positive" }
        return repository.update(id, dto)
    }

    fun delete(id: Int): Boolean {
        require(id > 0) { "ID must be positive" }
        return repository.delete(id)
    }

    fun updateReservation(id: Int, dto: UpdateBookCopyReservationDTO): Boolean {
        require(id > 0) { "ID must be positive" }
        return repository.updateReservation(id, dto.is_in_reservation)
    }

    fun updateLocation(id: Int, dto: UpdateBookCopyLocationDTO): Boolean {
        require(id > 0) { "ID must be positive" }
        return repository.updateLocation(id, dto.is_in_here)
    }

    fun updateBorrowedStatus(id: Int, dto: UpdateBookCopyBorrowedStatusDTO): Boolean {
        require(id > 0) { "ID must be positive" }
        return repository.updateBorrowedStatus(id, dto.borrowed_By_Other_User)
    }
} 