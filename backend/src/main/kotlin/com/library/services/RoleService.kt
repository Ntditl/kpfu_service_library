package com.library.services

import com.library.models.RoleDTO
import com.library.repositories.RoleRepository

class RoleService(
    private val repository: RoleRepository = RoleRepository()
) {

    fun getAll(): List<RoleDTO> {
        return repository.getAll()
    }

    fun getById(id: Int): RoleDTO? {
        require(id > 0) { "ID must be positive" }
        return repository.getById(id)
    }


    fun create(dto: RoleDTO): Int {
        require(dto.role_name.isNotBlank()) { "Role name must not be blank" }
        return repository.create(dto)
    }

    fun update(id: Int, dto: RoleDTO): Boolean {
        require(dto.role_name.isNotBlank()) { "Role name must not be blank" }
        return repository.update(id, dto)
    }

    fun delete(id: Int): Boolean {
        require(id > 0) { "ID must be positive" }
        return repository.delete(id)
    }
}
