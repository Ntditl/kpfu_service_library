package com.library.routes

import com.library.models.CreateUserRequest
import com.library.models.UserDTO
import com.library.models.CreateUserDTO
import com.library.models.UpdateUserDTO
import com.library.models.UpdateUserRoleDTO
import com.library.services.UserService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory

fun Route.userRoutes() {
    val service = UserService()
    val logger = LoggerFactory.getLogger("UserRoutes")

    route("/users") {
        get {
            try {
                logger.info("Received request to get all users")
                val users = service.getAll()
                logger.info("Successfully retrieved ${users.size} users")
                call.respond(users)
            } catch (e: Exception) {
                logger.error("Error getting users", e)
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }

            val user = service.getById(id)
            if (user == null) {
                call.respond(HttpStatusCode.NotFound, "User not found")
            } else {
                call.respond(user)
            }
        }

        post {
            try {
                logger.info("Received user creation request")
                val request = call.receive<CreateUserRequest>()
                logger.info("Parsed request: email=${request.email}, firstName=${request.first_name}, lastName=${request.last_name}")
                
                // Hash the password
                val passwordHash = BCrypt.hashpw(request.password, BCrypt.gensalt())
                logger.info("Password hashed successfully")
                
                // Convert request to UserDTO
                val userDto = UserDTO(
                    email = request.email,
                    first_name = request.first_name,
                    last_name = request.last_name,
                    phone = request.phone,
                    role_id = request.role_id,
                    is_active = request.is_active
                )
                logger.info("Created UserDTO object")
                
                val newId = service.create(userDto, passwordHash)
                logger.info("User created successfully with ID: $newId")
                
                call.respond(HttpStatusCode.Created, mapOf("id" to newId))
            } catch (e: Exception) {
                logger.error("Error creating user", e)
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val dto = call.receive<UpdateUserDTO>()
            val updated = service.update(id, dto)
            if (updated) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        }

        // Новый эндпоинт для изменения роли
        put("/{id}/role") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@put
            }

            val dto = call.receive<UpdateUserRoleDTO>()
            val updated = service.updateRole(id, dto)
            if (updated) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
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
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        }
    }
}