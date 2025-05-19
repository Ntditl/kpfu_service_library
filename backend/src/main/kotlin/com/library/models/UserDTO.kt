package com.library.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val user_id: Int? = null,
    val email: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val role_id: Int = 1,
    val is_active: Boolean = true
)