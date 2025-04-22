package com.library.models


import kotlinx.serialization.Serializable

@Serializable
data class RoleDTO(
    val role_id: Int? = null,
    val role_name: String
)
