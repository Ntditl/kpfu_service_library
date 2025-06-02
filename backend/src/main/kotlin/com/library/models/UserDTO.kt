package com.library.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val user_id: Int? = null,
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val role_id: Int,
    val is_active: Boolean,
    val password_hash: String? = null
)

@Serializable
data class CreateUserDTO(
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val role_id: Int,
    val is_active: Boolean
)

@Serializable
data class UpdateUserDTO(
    val email: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val phone: String? = null,
    val role_id: Int? = null,
    val is_active: Boolean? = null,
    val password_hash: String? = null
)

@Serializable
data class UpdateUserRoleDTO(
    val role_id: Int
)