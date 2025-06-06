package com.library.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime

@Serializable
data class BorrowingDTO(
    val reservation_id: Int? = null,
    val book_copy_id: Int,
    val user_id: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date_request_from_user: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date_answer_to_request: LocalDateTime? = null,
    val res_answer_to_user: Boolean? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date_of_start_of_issuance: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date_to_return: LocalDateTime? = null
)

@Serializable
data class UpdateBorrowingDTO(
    val book_copy_id: Int? = null,
    val user_id: Int? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date_request_from_user: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date_answer_to_request: LocalDateTime? = null,
    val res_answer_to_user: Boolean? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date_of_start_of_issuance: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date_to_return: LocalDateTime? = null
)

@Serializable
data class CreateBorrowingByBookRequest(
    val book_id: Int,
    val user_id: Int,
    val date_request_from_user: String
)

@Serializable
data class UpdateBorrowingReturnDateDTO(
    @Serializable(with = LocalDateTimeSerializer::class)
    val date_to_return: LocalDateTime
)

@Serializable
data class BorrowingsToRoleDTO(
    val role_reservation_id: Int? = null,
    val role_id: Int,
    val user_id: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date_request_from: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date_answer_to_request: LocalDateTime? = null,
    val res_answer_to_request: Boolean? = null
)

@Serializable
data class CreateBorrowingsToRoleDTO(
    val role_id: Int,
    val user_id: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date_request_from: LocalDateTime
)

@Serializable
data class UpdateBorrowingsToRoleDTO(
    val role_id: Int? = null,
    val user_id: Int? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date_request_from: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date_answer_to_request: LocalDateTime? = null,
    val res_answer_to_request: Boolean? = null
)

class LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString())
    }
} 