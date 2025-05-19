package com.library.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val first_name: String,
    val last_name: String,
    val phone: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val user: UserDTO
) 