package com.library.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val email: String,
    val password: String,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val role_id: Int = 1,
    val is_active: Boolean = true
) 