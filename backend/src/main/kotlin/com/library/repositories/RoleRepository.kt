package com.library.repositories

import com.library.database.Roles
import com.library.models.RoleDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq



class RoleRepository {

    fun getAll(): List<RoleDTO> = transaction {
        Roles.selectAll().map {
            RoleDTO(
                role_id = it[Roles.roleId],
                role_name = it[Roles.roleName]
            )
        }
    }

    fun getById(id: Int): RoleDTO? = transaction {
        Roles.select { Roles.roleId eq id }
            .map {
                RoleDTO(
                    role_id = it[Roles.roleId],
                    role_name = it[Roles.roleName]
                )
            }
            .singleOrNull()
    }

    fun create(role: RoleDTO): Int = transaction {
        Roles.insert {
            it[roleName] = role.role_name
        } get Roles.roleId
    }

    fun update(id: Int, role: RoleDTO): Boolean = transaction {
        Roles.update({ Roles.roleId eq id }) {
            it[roleName] = role.role_name
        } > 0
    }

    fun delete(id: Int): Boolean = transaction {
        Roles.deleteWhere { Roles.roleId eq id } > 0
    }
}
