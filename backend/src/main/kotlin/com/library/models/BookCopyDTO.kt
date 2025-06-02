package com.library.models

import kotlinx.serialization.Serializable

@Serializable
data class BookCopyDTO(
    val copy_id: Int? = null,
    val book_id: Int,
    val is_in_here: Boolean,
    val is_in_reservation: Boolean,
    val borrowed_By_Other_User: Boolean? = false
)

@Serializable
data class UpdateBookCopyReservationDTO(
    val is_in_reservation: Boolean
)

@Serializable
data class UpdateBookCopyLocationDTO(
    val is_in_here: Boolean
)

@Serializable
data class UpdateBookCopyBorrowedStatusDTO(
    val borrowed_By_Other_User: Boolean
) 