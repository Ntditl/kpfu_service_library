package com.library.models

import kotlinx.serialization.Serializable

@Serializable
data class BookDTO(
    val bookId: Int? = null,
    val title: String,
    val author: String?,        
    val isbn: String?,
    val publicationYear: Int?,
    val publisher: String?,
    val genre: String?,
    val description: String?,
    val keywords: String?,
    val coverUrl: String?
)
