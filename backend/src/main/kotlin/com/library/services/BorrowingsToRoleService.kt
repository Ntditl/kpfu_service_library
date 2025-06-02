package com.library.services

import com.library.models.BorrowingsToRoleDTO
import com.library.models.CreateBorrowingsToRoleDTO
import com.library.models.UpdateBorrowingsToRoleDTO
import com.library.repositories.BorrowingsToRoleRepository

class BorrowingsToRoleService(
    private val repository: BorrowingsToRoleRepository = BorrowingsToRoleRepository()
) {

    fun getAll(): List<BorrowingsToRoleDTO> {
        return repository.getAll()
    }

    fun getById(id: Int): BorrowingsToRoleDTO? {
        require(id > 0) { "ID must be positive" }
        return repository.getById(id)
    }

    fun create(dto: CreateBorrowingsToRoleDTO): Int {
        require(dto.role_id > 0) { "Role ID must be positive" }
        require(dto.user_id > 0) { "User ID must be positive" }
        return repository.create(dto)
    }

    fun update(id: Int, dto: UpdateBorrowingsToRoleDTO): Boolean {
        require(id > 0) { "ID must be positive" }
        dto.role_id?.let { require(it > 0) { "Role ID must be positive" } }
        dto.user_id?.let { require(it > 0) { "User ID must be positive" } }
        return repository.update(id, dto)
    }

    fun delete(id: Int): Boolean {
        require(id > 0) { "ID must be positive" }
        return repository.delete(id)
    }

    fun getByUserId(userId: Int): List<BorrowingsToRoleDTO> {
        require(userId > 0) { "User ID must be positive" }
        return repository.getByUserId(userId)
    }
} 