package com.library.services

import com.library.models.UserDTO
import com.library.repositories.UserRepository
import org.slf4j.LoggerFactory

class UserService(
    private val repository: UserRepository = UserRepository()
) {
    private val logger = LoggerFactory.getLogger("UserService")

    fun create(user: UserDTO, passwordHash: String): Int {
        logger.info("Creating new user with email: ${user.email}")
        return try {
            val newId = repository.create(user, passwordHash)
            logger.info("User created successfully with ID: $newId")
            newId
        } catch (e: Exception) {
            logger.error("Failed to create user", e)
            throw e
        }
    }

    fun getAll(): List<UserDTO> {
        logger.info("Getting all users")
        return try {
            repository.getAll()
        } catch (e: Exception) {
            logger.error("Failed to get users", e)
            throw e
        }
    }
}