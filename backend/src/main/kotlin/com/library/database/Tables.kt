package com.library.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Roles : Table("roles") {
    val roleId = integer("role_id").autoIncrement()
    val roleName = varchar("role_name", 50).uniqueIndex()

    override val primaryKey = PrimaryKey(roleId)
}

object Books : Table(name = "books") {
    val bookId = integer("book_id").autoIncrement()
    val title = varchar("title", 200)
    val author = varchar("author", 200).nullable()
    val isbn = varchar("isbn", 20).nullable()
    val publicationYear = integer("publication_year").nullable()
    val publisher = varchar("publisher", 150).nullable()
    val genre = varchar("genre", 100).nullable()
    val description = text("description").nullable()
    val keywords = text("keywords").nullable()
    val coverUrl = varchar("cover_url", 500).nullable()

    override val primaryKey = PrimaryKey(bookId)
}

object Users : Table("users") {
    val userId = integer("user_id").autoIncrement()
    val email = varchar("email", 100).uniqueIndex()
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val phone = varchar("phone", 20)
    val roleId = integer("role_id")
    val isActive = bool("is_active")
    val passwordHash = varchar("PasswordHash", 100)

    override val primaryKey = PrimaryKey(userId)
}
