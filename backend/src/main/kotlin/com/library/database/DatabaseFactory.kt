package com.library.database


import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils

object DatabaseFactory {
    fun init(environment: ApplicationEnvironment) {
        val config = environment.config

        val url = config.property("ktor.database.url").getString()
        val driver = config.property("ktor.database.driver").getString()
        val user = config.property("ktor.database.user").getString()
        val password = config.property("ktor.database.password").getString()

        Database.connect(url = url, driver = driver, user = user, password = password)

        transaction {
            // Drop tables in correct order (child tables first, then parent tables)
            exec("DROP TABLE IF EXISTS book_copies CASCADE")
            exec("DROP TABLE IF EXISTS reservations CASCADE")
            exec("DROP TABLE IF EXISTS books CASCADE")
            exec("DROP TABLE IF EXISTS users CASCADE")
            exec("DROP TABLE IF EXISTS roles CASCADE")

            // Create tables with correct column names
            SchemaUtils.create(
                Roles, Users, Books
                //BookCopies, Borrowings, Reservations, ReadingRoomVisits, Notifications
            )
        }
    }
}
