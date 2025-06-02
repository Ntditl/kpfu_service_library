package com.library.repositories

import com.library.database.Users
import com.library.models.UserDTO
import com.library.models.CreateUserDTO
import com.library.models.UpdateUserDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.slf4j.LoggerFactory

class UserRepository {
    private val logger = LoggerFactory.getLogger("UserRepository")

    fun create(user: UserDTO, passwordHash: String): Int = transaction {
        logger.info("Starting database transaction to create user: ${user.email}")
        try {
            val userId = Users.insert {
                it[email] = user.email
                it[firstName] = user.first_name
                it[lastName] = user.last_name
                it[phone] = user.phone
                it[roleId] = user.role_id
                it[isActive] = user.is_active
                it[Users.passwordHash] = passwordHash
            } get Users.userId
            logger.info("User created in database with ID: $userId")
            userId
        } catch (e: Exception) {
            logger.error("Database error while creating user", e)
            throw e
        }
    }

    fun findByEmail(email: String): UserDTO? = transaction {
        logger.info("Searching for user by email: $email")
        try {
            Users.select { Users.email eq email }
                .map { row ->
                    UserDTO(
                        user_id = row[Users.userId],
                        email = row[Users.email],
                        first_name = row[Users.firstName],
                        last_name = row[Users.lastName],
                        phone = row[Users.phone],
                        role_id = row[Users.roleId],
                        is_active = row[Users.isActive]
                    )
                }
                .firstOrNull()
                .also { logger.info("User ${if (it == null) "not " else ""}found") }
        } catch (e: Exception) {
            logger.error("Database error while searching user by email", e)
            throw e
        }
    }

    fun findById(id: Int): UserDTO? = transaction {
        logger.info("Searching for user by ID: $id")
        try {
            Users.select { Users.userId eq id }
                .map { row ->
                    UserDTO(
                        user_id = row[Users.userId],
                        email = row[Users.email],
                        first_name = row[Users.firstName],
                        last_name = row[Users.lastName],
                        phone = row[Users.phone],
                        role_id = row[Users.roleId],
                        is_active = row[Users.isActive]
                    )
                }
                .firstOrNull()
                .also { logger.info("User ${if (it == null) "not " else ""}found") }
        } catch (e: Exception) {
            logger.error("Database error while searching user by ID", e)
            throw e
        }
    }

    fun findUserWithPasswordHash(email: String): Pair<UserDTO, String>? = transaction {
        logger.info("Searching for user with password hash by email: $email")
        try {
            Users.select { Users.email eq email }
                .map { row ->
                    Pair(
                        UserDTO(
                            user_id = row[Users.userId],
                            email = row[Users.email],
                            first_name = row[Users.firstName],
                            last_name = row[Users.lastName],
                            phone = row[Users.phone],
                            role_id = row[Users.roleId],
                            is_active = row[Users.isActive]
                        ),
                        row[Users.passwordHash]
                    )
                }
                .firstOrNull()
                .also { logger.info("User with password hash ${if (it == null) "not " else ""}found") }
        } catch (e: Exception) {
            logger.error("Database error while searching user with password hash", e)
            throw e
        }
    }

    fun getAll(): List<UserDTO> = transaction {
        logger.info("Getting all users from database")
        try {
            Users.selectAll()
                .map { row ->
                    UserDTO(
                        user_id = row[Users.userId],
                        email = row[Users.email],
                        first_name = row[Users.firstName],
                        last_name = row[Users.lastName],
                        phone = row[Users.phone],
                        role_id = row[Users.roleId],
                        is_active = row[Users.isActive]
                    )
                }
                .also { logger.info("Found ${it.size} users") }
        } catch (e: Exception) {
            logger.error("Database error while getting all users", e)
            throw e
        }
    }

    fun update(id: Int, dto: UpdateUserDTO): Boolean = transaction {
        logger.info("Updating user with ID: $id in database")
        try {
            Users.update({ Users.userId eq id }) { stmt ->
                dto.email?.let { stmt[Users.email] = it }
                dto.first_name?.let { stmt[Users.firstName] = it }
                dto.last_name?.let { stmt[Users.lastName] = it }
                dto.phone?.let { stmt[Users.phone] = it }
                dto.role_id?.let { stmt[Users.roleId] = it }
                dto.is_active?.let { stmt[Users.isActive] = it }
                dto.password_hash?.let { stmt[Users.passwordHash] = it }
            } > 0
        } catch (e: Exception) {
            logger.error("Database error while updating user with ID: $id", e)
            throw e
        }
    }

    fun delete(id: Int): Boolean = transaction {
        logger.info("Deleting user with ID: $id from database")
        try {
            Users.deleteWhere { Users.userId eq id } > 0
        } catch (e: Exception) {
            logger.error("Database error while deleting user with ID: $id", e)
            throw e
        }
    }

    fun updateRole(id: Int, roleId: Int): Boolean = transaction {
        logger.info("Updating role for user with ID: $id to role ID: $roleId in database")
        try {
            Users.update({ Users.userId eq id }) {
                it[Users.roleId] = roleId
            } > 0
        } catch (e: Exception) {
            logger.error("Database error while updating role for user with ID: $id", e)
            throw e
        }
    }

    private fun toUserDTO(row: ResultRow): UserDTO {
        return UserDTO(
            user_id = row[Users.userId],
            email = row[Users.email],
            first_name = row[Users.firstName],
            last_name = row[Users.lastName],
            phone = row[Users.phone],
            role_id = row[Users.roleId],
            is_active = row[Users.isActive],
            password_hash = row[Users.passwordHash]
        )
    }
}