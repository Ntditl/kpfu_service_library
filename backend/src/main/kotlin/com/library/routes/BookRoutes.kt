package com.library.routes

import com.library.models.BookDTO
import com.library.services.BookService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun Route.bookRoutes() {
    val service = BookService()

    route("/books") {

        get {
            call.respond(service.getAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val book = service.getById(id)
            if (book == null) {
                call.respond(HttpStatusCode.NotFound, "Book not found")
            } else {
                call.respond(book)
            }
        }

        post {
            val dto = call.receive<BookDTO>()
            val newId = service.create(dto)
            call.respond(HttpStatusCode.Created, mapOf("id" to newId))
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val dto = call.receive<BookDTO>()
            val updated = service.update(id, dto)
            if (updated) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Book not found")
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@delete
            }

            val deleted = service.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Book not found")
            }
        }
    }
}
