package com.library.services

import com.library.models.BookDTO
import com.library.repositories.BookRepository

class BookService(
    private val repo: BookRepository = BookRepository()
) {

    fun getAll(): List<BookDTO> = repo.getAll()

    fun getById(id: Int): BookDTO? {
        require(id > 0) { "ID must be positive" }
        return repo.getById(id)
    }

    fun create(dto: BookDTO): Int {
        require(dto.title.isNotBlank()) { "Title must not be blank" }
        return repo.create(dto)
    }

    fun update(id: Int, dto: BookDTO): Boolean {
        require(dto.title.isNotBlank()) { "Title must not be blank" }
        return repo.update(id, dto)
    }

    fun delete(id: Int): Boolean {
        require(id > 0) { "ID must be positive" }
        return repo.delete(id)
    }
}
