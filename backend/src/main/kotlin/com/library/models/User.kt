package com.library.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table

object Users : UUIDTable() {
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val email = varchar("email", 100).uniqueIndex()
    val password = varchar("password", 100)
    val phone = varchar("phone", 20)
    val createdAt = long("created_at")
}

@Serializable
data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val createdAt: Long
)

@Serializable
data class UserCreate(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String
)

@Serializable
data class UserLogin(
    val email: String,
    val password: String
) 