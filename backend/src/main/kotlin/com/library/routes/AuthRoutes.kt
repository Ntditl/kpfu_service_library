package com.library.routes

import com.library.models.AuthResponse
import com.library.models.LoginRequest
import com.library.models.RegisterRequest
import com.library.services.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Route.authRoutes() {
    val authService = AuthService()
    val logger = LoggerFactory.getLogger("AuthRoutes")

    route("/auth") {
        options {
            logger.info("Received OPTIONS request for /auth")
            call.respond(HttpStatusCode.OK)
        }

        post("/register") {
            try {
                logger.info("Received registration request")
                val request = call.receive<RegisterRequest>()
                logger.info("Parsed registration request for email: ${request.email}")
                val response = authService.register(request)
                logger.info("User registered successfully")
                call.respond(HttpStatusCode.Created, response)
            } catch (e: IllegalArgumentException) {
                logger.warn("Registration failed: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, mapOf("message" to e.message))
            } catch (e: Exception) {
                logger.error("Registration error", e)
                call.respond(HttpStatusCode.InternalServerError, mapOf("message" to "Внутренняя ошибка сервера"))
            }
        }

        post("/login") {
            try {
                logger.info("Received login request")
                val request = call.receive<LoginRequest>()
                logger.info("Parsed login request for email: ${request.email}")
                val response = authService.login(request)
                logger.info("User logged in successfully")
                call.respond(HttpStatusCode.OK, response)
            } catch (e: IllegalArgumentException) {
                logger.warn("Login failed: ${e.message}")
                call.respond(HttpStatusCode.BadRequest, mapOf("message" to e.message))
            } catch (e: Exception) {
                logger.error("Login error", e)
                call.respond(HttpStatusCode.InternalServerError, mapOf("message" to "Внутренняя ошибка сервера"))
            }
        }
    }
} 