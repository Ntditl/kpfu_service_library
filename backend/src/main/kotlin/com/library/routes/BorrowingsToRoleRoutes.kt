package com.library.routes

import com.library.models.BorrowingsToRoleDTO
import com.library.models.CreateBorrowingsToRoleDTO
import com.library.models.UpdateBorrowingsToRoleDTO
import com.library.services.BorrowingsToRoleService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun Route.borrowingsToRoleRoutes() {
    val service = BorrowingsToRoleService()

    route("/borrowings-to-role") {

        get {
            call.respond(service.getAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val borrowingsToRole = service.getById(id)
            if (borrowingsToRole == null) {
                call.respond(HttpStatusCode.NotFound, "Borrowings to role not found")
            } else {
                call.respond(borrowingsToRole)
            }
        }

        post {
            val dto = call.receive<CreateBorrowingsToRoleDTO>()
            val newId = service.create(dto)
            call.respond(HttpStatusCode.Created, mapOf("id" to newId))
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val dto = call.receive<UpdateBorrowingsToRoleDTO>()
            val updated = service.update(id, dto)
            if (updated) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Borrowings to role not found")
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
                call.respond(HttpStatusCode.NotFound, "Borrowings to role not found")
            }
        }
    }
} 