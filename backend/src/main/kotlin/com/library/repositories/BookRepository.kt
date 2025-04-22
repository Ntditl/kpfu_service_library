package com.library.repositories

import com.library.database.Books
import com.library.models.BookDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class BookRepository {

    fun getAll(): List<BookDTO> = transaction {
        Books.selectAll().map {
            BookDTO(
                bookId = it[Books.bookId],
                title = it[Books.title],
                author = it[Books.author],
                isbn = it[Books.isbn],
                publicationYear = it[Books.publicationYear],
                publisher = it[Books.publisher],
                genre = it[Books.genre],
                description = it[Books.description],
                keywords = it[Books.keywords],
                coverUrl = it[Books.coverUrl]
            )
        }
    }

    fun getById(id: Int): BookDTO? = transaction {
        Books
            .select { Books.bookId eq id }
            .map {
                BookDTO(
                    bookId = it[Books.bookId],
                    title = it[Books.title],
                    author = it[Books.author],
                    isbn = it[Books.isbn],
                    publicationYear = it[Books.publicationYear],
                    publisher = it[Books.publisher],
                    genre = it[Books.genre],
                    description = it[Books.description],
                    keywords = it[Books.keywords],
                    coverUrl = it[Books.coverUrl]
                )
            }
            .singleOrNull()
    }

    fun create(dto: BookDTO): Int = transaction {
        Books.insert {
            it[title] = dto.title
            it[author] = dto.author
            it[isbn] = dto.isbn
            it[publicationYear] = dto.publicationYear
            it[publisher] = dto.publisher
            it[genre] = dto.genre
            it[description] = dto.description
            it[keywords] = dto.keywords
            it[coverUrl] = dto.coverUrl
        } get Books.bookId
    }

    fun update(id: Int, dto: BookDTO): Boolean = transaction {
        Books.update({ Books.bookId eq id }) {
            it[title] = dto.title
            it[author] = dto.author
            it[isbn] = dto.isbn
            it[publicationYear] = dto.publicationYear
            it[publisher] = dto.publisher
            it[genre] = dto.genre
            it[description] = dto.description
            it[keywords] = dto.keywords
            it[coverUrl] = dto.coverUrl
        } > 0
    }

    fun delete(id: Int): Boolean = transaction {
        Books.deleteWhere { Books.bookId eq id } > 0
    }
}

