package com.library.services

import com.library.models.UserDTO
import com.library.models.CreateUserDTO
import com.library.models.UpdateUserDTO
import com.library.models.UpdateUserRoleDTO
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

    fun getById(id: Int): UserDTO? {
        require(id > 0) { "User ID must be positive" }
        logger.info("Getting user by ID: $id")
        return try {
            repository.findById(id)
        } catch (e: Exception) {
            logger.error("Failed to get user by ID", e)
            throw e
        }
    }

    fun update(id: Int, dto: UpdateUserDTO): Boolean {
        require(id > 0) { "User ID must be positive" }
        logger.info("Updating user with ID: $id")
        // В UpdateUserDTO поля nullable, поэтому проверки на положительность ID в них не нужны на уровне сервиса
        return try {
            repository.update(id, dto)
        } catch (e: Exception) {
            logger.error("Failed to update user with ID: $id", e)
            throw e
        }
    }

    fun delete(id: Int): Boolean {
        require(id > 0) { "User ID must be positive" }
        logger.info("Deleting user with ID: $id")
        return try {
            repository.delete(id)
        } catch (e: Exception) {
            logger.error("Failed to delete user with ID: $id", e)
            throw e
        }
    }

    fun updateRole(id: Int, dto: UpdateUserRoleDTO): Boolean {
        require(id > 0) { "User ID must be positive" }
        require(dto.role_id > 0) { "Role ID must be positive" }
        logger.info("Updating role for user with ID: $id to role ID: ${dto.role_id}")
        return try {
            repository.updateRole(id, dto.role_id)
        } catch (e: Exception) {
            logger.error("Failed to update role for user with ID: $id", e)
            throw e
        }
    }
}