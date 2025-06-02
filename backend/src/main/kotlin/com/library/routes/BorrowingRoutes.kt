package com.library.routes

import com.library.models.BorrowingDTO
import com.library.models.UpdateBorrowingDTO
import com.library.services.BorrowingService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*
import java.time.LocalDateTime
import io.ktor.server.plugins.contentnegotiation.*
import com.library.models.CreateBorrowingByBookRequest
import kotlinx.serialization.SerializationException

fun Route.borrowingRoutes() {
    val service = BorrowingService()

    route("/borrowings") {
        get {
            call.respond(service.getAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val borrowing = service.getById(id)
            if (borrowing == null) {
                call.respond(HttpStatusCode.NotFound, "Borrowing not found")
            } else {
                call.respond(borrowing)
            }
        }

        post {
            try {
                val dto = call.receive<BorrowingDTO>()
                val newId = service.create(dto)
                call.respond(HttpStatusCode.Created, mapOf("id" to newId))
            } catch (e: Exception) {
                call.application.environment.log.error("Error creating borrowing", e)
                call.respond(HttpStatusCode.BadRequest, "Error creating borrowing: ${e.message}")
            }
        }

        post("/by-book") {
            try {
                val request = call.receive<CreateBorrowingByBookRequest>()
                call.application.environment.log.info("Received /by-book request body: $request")

                val bookId = request.book_id
                val userId = request.user_id
                val dateStr = request.date_request_from_user

                if (bookId == null || userId == null || dateStr == null) {
                    call.application.environment.log.warn("Missing fields in /by-book request: bookId=$bookId, userId=$userId, dateStr=$dateStr")
                    call.respond(HttpStatusCode.BadRequest, "Missing required fields")
                    return@post
                }

                try {
                    val date = LocalDateTime.parse(dateStr)
                    val newId = service.createByBookId(bookId, userId, date)
                    call.respond(HttpStatusCode.Created, mapOf("id" to newId))
                } catch (e: IllegalStateException) {
                    call.application.environment.log.warn("No available copies for bookId $bookId", e)
                    call.respond(HttpStatusCode.NotFound, e.message ?: "No available copies")
                } catch (e: IllegalArgumentException) {
                     call.application.environment.log.warn("Invalid arguments for /by-book request: bookId=$bookId, userId=$userId", e)
                     call.respond(HttpStatusCode.BadRequest, "Invalid input: ${e.message}")
                }
            } catch (e: SerializationException) {
                call.application.environment.log.error("Serialization error processing /by-book request", e)
                call.respond(HttpStatusCode.BadRequest, "Invalid request format: ${e.message}")
            } catch (e: Exception) {
                 call.application.environment.log.error("Error processing /by-book request", e)
                 call.respond(HttpStatusCode.BadRequest, "Error processing request: ${e.message}")
            }
        }

        put("/{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@put
                }

                val dto = call.receive<UpdateBorrowingDTO>()
                val updated = service.update(id, dto)
                if (updated) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Borrowing not found")
                }
            } catch (e: Exception) {
                call.application.environment.log.error("Error updating borrowing", e)
                call.respond(HttpStatusCode.BadRequest, "Error updating borrowing: ${e.message}")
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
                call.respond(HttpStatusCode.NotFound, "Borrowing not found")
            }
        }
    }
} 