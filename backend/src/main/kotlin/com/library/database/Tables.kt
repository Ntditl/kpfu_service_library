package com.library.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.datetime

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

object BookCopies : Table("book_copies") {
    val copyId = integer("copy_id").autoIncrement()
    val bookId = integer("book_id").references(Books.bookId)
    val isInHere = bool("is_in_here")
    val isInReservation = bool("is_in_reservation")
    val isInBorrowedByUser = bool("borrowed_by_other_user").default(false)

    override val primaryKey = PrimaryKey(copyId)
}

object Borrowings : Table("borrowings") {
    val reservationId = integer("reservation_id").autoIncrement()
    val bookCopyId = integer("book_copy_id").references(BookCopies.copyId)
    val userId = integer("user_id").references(Users.userId)
    val dateRequestFromUser = datetime("date_request_from_user")
    val dateAnswerToRequest = datetime("date_answer_to_request").nullable()
    val resAnswerToUser = bool("res_answer_to_user").nullable()
    val dateOfStartOfIssuance = datetime("date_of_start_of_issuance").nullable()
    val dateToReturn = datetime("date_to_return").nullable()

    override val primaryKey = PrimaryKey(reservationId)
}

object BorrowingsToRole : Table("borrowings_to_role") {
    val roleReservationId = integer("role_reservation_id").autoIncrement()
    val roleId = integer("role_id").references(Roles.roleId)
    val userId = integer("user_id").references(Users.userId)
    val dateRequestFrom = datetime("date_request_from")
    val dateAnswerToRequest = datetime("date_answer_to_request").nullable()
    val resAnswerToRequest = bool("res_answer_to_request").nullable()

    override val primaryKey = PrimaryKey(roleReservationId)
}
