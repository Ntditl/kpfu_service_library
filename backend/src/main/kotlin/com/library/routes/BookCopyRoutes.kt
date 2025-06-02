package com.library.routes

import com.library.models.BookCopyDTO
import com.library.models.UpdateBookCopyReservationDTO
import com.library.models.UpdateBookCopyLocationDTO
import com.library.models.UpdateBookCopyBorrowedStatusDTO
import com.library.services.BookCopyService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun Route.bookCopyRoutes() {
    val service = BookCopyService()

    route("/book-copies") {

        get {
            call.respond(service.getAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val bookCopy = service.getById(id)
            if (bookCopy == null) {
                call.respond(HttpStatusCode.NotFound, "Book copy not found")
            } else {
                call.respond(bookCopy)
            }
        }

        post {
            val dto = call.receive<BookCopyDTO>()
            val newId = service.create(dto)
            call.respond(HttpStatusCode.Created, mapOf("id" to newId))
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val dto = call.receive<BookCopyDTO>()
            val updated = service.update(id, dto)
            if (updated) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Book copy not found")
            }
        }

        put("/{id}/reservation") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val dto = call.receive<UpdateBookCopyReservationDTO>()
            val updated = service.updateReservation(id, dto)
            if (updated) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Book copy not found")
            }
        }

        put("/{id}/location") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val dto = call.receive<UpdateBookCopyLocationDTO>()
            val updated = service.updateLocation(id, dto)
            if (updated) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Book copy not found")
            }
        }

        put("/{id}/borrowed-status") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val dto = call.receive<UpdateBookCopyBorrowedStatusDTO>()
            val updated = service.updateBorrowedStatus(id, dto)
            if (updated) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Book copy not found")
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
                call.respond(HttpStatusCode.NotFound, "Book copy not found")
            }
        }
    }
} 