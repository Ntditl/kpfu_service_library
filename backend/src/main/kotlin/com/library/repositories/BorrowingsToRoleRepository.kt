package com.library.repositories

import com.library.database.BorrowingsToRole
import com.library.models.BorrowingsToRoleDTO
import com.library.models.CreateBorrowingsToRoleDTO
import com.library.models.UpdateBorrowingsToRoleDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime

class BorrowingsToRoleRepository {

    fun getAll(): List<BorrowingsToRoleDTO> = transaction {
        BorrowingsToRole.selectAll().map { toBorrowingsToRoleDTO(it) }
    }

    fun getById(id: Int): BorrowingsToRoleDTO? = transaction {
        BorrowingsToRole.select { BorrowingsToRole.roleReservationId eq id }
            .map { toBorrowingsToRoleDTO(it) }
            .singleOrNull()
    }

    fun create(dto: CreateBorrowingsToRoleDTO): Int = transaction {
        BorrowingsToRole.insert {
            it[roleId] = dto.role_id
            it[userId] = dto.user_id
            it[dateRequestFrom] = dto.date_request_from
        } get BorrowingsToRole.roleReservationId
    }

    fun update(id: Int, dto: UpdateBorrowingsToRoleDTO): Boolean = transaction {
        BorrowingsToRole.update({ BorrowingsToRole.roleReservationId eq id }) { stmt ->
            dto.role_id?.let { roleId -> stmt[BorrowingsToRole.roleId] = roleId }
            dto.user_id?.let { userId -> stmt[BorrowingsToRole.userId] = userId }
            dto.date_request_from?.let { date -> stmt[BorrowingsToRole.dateRequestFrom] = date }
            dto.date_answer_to_request?.let { date -> stmt[BorrowingsToRole.dateAnswerToRequest] = date }
            dto.res_answer_to_request?.let { answer -> stmt[BorrowingsToRole.resAnswerToRequest] = answer }
        } > 0
    }

    fun delete(id: Int): Boolean = transaction {
        BorrowingsToRole.deleteWhere { BorrowingsToRole.roleReservationId eq id } > 0
    }

    fun getByUserId(userId: Int): List<BorrowingsToRoleDTO> = transaction {
        BorrowingsToRole.select { BorrowingsToRole.userId eq userId }
            .map { toBorrowingsToRoleDTO(it) }
    }

    private fun toBorrowingsToRoleDTO(row: ResultRow): BorrowingsToRoleDTO {
        return BorrowingsToRoleDTO(
            role_reservation_id = row[BorrowingsToRole.roleReservationId],
            role_id = row[BorrowingsToRole.roleId],
            user_id = row[BorrowingsToRole.userId],
            date_request_from = row[BorrowingsToRole.dateRequestFrom],
            date_answer_to_request = row[BorrowingsToRole.dateAnswerToRequest],
            res_answer_to_request = row[BorrowingsToRole.resAnswerToRequest]
        )
    }
} 