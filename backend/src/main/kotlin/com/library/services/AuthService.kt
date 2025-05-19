package com.library.services

import com.library.models.AuthResponse
import com.library.models.LoginRequest
import com.library.models.RegisterRequest
import com.library.models.UserDTO
import com.library.repositories.UserRepository
import io.ktor.server.auth.jwt.*
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import java.util.*

class AuthService(
    private val userRepository: UserRepository = UserRepository(),
    private val jwtService: JwtService = JwtService()
) {
    private val logger = LoggerFactory.getLogger("AuthService")

    fun register(request: RegisterRequest): AuthResponse {
        logger.info("Starting registration for email: ${request.email}")
        // Проверяем, не существует ли уже пользователь с таким email
        if (userRepository.findByEmail(request.email) != null) {
            logger.warn("Registration failed: user with email ${request.email} already exists")
            throw IllegalArgumentException("Пользователь с таким email уже существует")
        }

        // Хэшируем пароль
        val passwordHash = BCrypt.hashpw(request.password, BCrypt.gensalt())
        logger.info("Password hashed successfully")

        // Создаем пользователя DTO (без пароля и firebase_uid)
        val userDto = UserDTO(
            email = request.email,
            first_name = request.first_name,
            last_name = request.last_name,
            phone = request.phone,
            role_id = 1, // По умолчанию обычный пользователь
            is_active = true
        )
        logger.info("Created UserDTO object")

        val userId = userRepository.create(userDto, passwordHash)
        logger.info("User created in database with ID: $userId")

        // Получаем созданного пользователя из базы для создания JWT токена с user_id
        val createdUser = userRepository.findById(userId) ?: throw IllegalStateException("Failed to retrieve created user")
        logger.info("Retrieved created user from database")

        // Генерируем JWT токен
        val token = jwtService.generateToken(createdUser)
        logger.info("JWT token generated successfully")

        return AuthResponse(token = token, user = createdUser)
    }

    fun login(request: LoginRequest): AuthResponse {
        logger.info("Starting login for email: ${request.email}")
        // Находим пользователя по email вместе с хэшем пароля
        val userWithHash = userRepository.findUserWithPasswordHash(request.email)
            ?: throw IllegalArgumentException("Пользователь не найден")
        logger.info("User found in database")

        val (user, passwordHash) = userWithHash

        // Проверяем пароль
        if (!BCrypt.checkpw(request.password, passwordHash)) {
            logger.warn("Login failed: invalid password for user ${request.email}")
            throw IllegalArgumentException("Неверный пароль")
        }
        logger.info("Password verified successfully")

        // Генерируем JWT токен
        val token = jwtService.generateToken(user)
        logger.info("JWT token generated successfully")

        return AuthResponse(token = token, user = user)
    }

    fun validateToken(token: String): UserDTO? {
        return try {
            logger.info("Validating token")
            val userId = jwtService.validateToken(token)
            logger.info("Token validated, retrieving user with ID: $userId")
            userRepository.findById(userId)
        } catch (e: Exception) {
            logger.error("Token validation failed", e)
            null
        }
    }
} 