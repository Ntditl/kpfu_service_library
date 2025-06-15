package com.library.services

import com.library.models.RoleDTO
import com.library.repositories.RoleRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*

class RoleServiceTest {

    private lateinit var roleRepository: RoleRepository
    private lateinit var roleService: RoleService

    @BeforeEach
    fun setup() {
        roleRepository = mock()
        roleService = RoleService(roleRepository)
    }

    @Test
    fun `должен получить список всех ролей`() {
        // Подготовка данных
        val roles = listOf(
            RoleDTO(role_id = 1, role_name = "Администратор"),
            RoleDTO(role_id = 2, role_name = "Библиотекарь"),
            RoleDTO(role_id = 3, role_name = "Читатель")
        )

        // Настройка поведения мока
        whenever(roleRepository.getAll()).thenReturn(roles)

        // Вызов тестируемого метода
        val result = roleService.getAll()

        // Проверка результата
        assert(result.size == 3)
        assert(result[0].role_name == "Администратор")
        assert(result[1].role_name == "Библиотекарь")
        assert(result[2].role_name == "Читатель")

        // Проверка вызова мока
        verify(roleRepository).getAll()
    }

    @Test
    fun `должен получить роль по ID`() {
        // Подготовка данных
        val roleId = 1
        val role = RoleDTO(role_id = roleId, role_name = "Администратор")

        // Настройка поведения мока
        whenever(roleRepository.getById(roleId)).thenReturn(role)

        // Вызов тестируемого метода
        val result = roleService.getById(roleId)

        // Проверка результата
        assert(result != null)
        assert(result?.role_id == roleId)
        assert(result?.role_name == "Администратор")

        // Проверка вызова мока
        verify(roleRepository).getById(roleId)
    }

    @Test
    fun `должен выбросить исключение при поиске роли с неположительным ID`() {
        // Проверка выброса исключения
        assertThrows<IllegalArgumentException> {
            roleService.getById(0)
        }

        // Проверка что мок не вызывался
        verify(roleRepository, never()).getById(any())
    }

    @Test
    fun `должен создать новую роль`() {
        // Подготовка данных
        val roleDTO = RoleDTO(role_name = "Новая роль")
        val createdRoleId = 4

        // Настройка поведения мока
        whenever(roleRepository.create(roleDTO)).thenReturn(createdRoleId)

        // Вызов тестируемого метода
        val result = roleService.create(roleDTO)

        // Проверка результата
        assert(result == createdRoleId)

        // Проверка вызова мока
        verify(roleRepository).create(roleDTO)
    }

    @Test
    fun `должен выбросить исключение при создании роли с пустым названием`() {
        // Подготовка данных
        val roleDTO = RoleDTO(role_name = "")  // Пустое название роли

        // Проверка выброса исключения
        assertThrows<IllegalArgumentException> {
            roleService.create(roleDTO)
        }

        // Проверка что мок не вызывался
        verify(roleRepository, never()).create(any())
    }

    @Test
    fun `должен обновить существующую роль`() {
        // Подготовка данных
        val roleId = 1
        val roleDTO = RoleDTO(role_name = "Администратор (обновлено)")

        // Настройка поведения мока
        whenever(roleRepository.update(roleId, roleDTO)).thenReturn(true)

        // Вызов тестируемого метода
        val result = roleService.update(roleId, roleDTO)

        // Проверка результата
        assert(result)

        // Проверка вызова мока
        verify(roleRepository).update(roleId, roleDTO)
    }

    @Test
    fun `должен удалить роль по ID`() {
        // Подготовка данных
        val roleId = 3

        // Настройка поведения мока
        whenever(roleRepository.delete(roleId)).thenReturn(true)

        // Вызов тестируемого метода
        val result = roleService.delete(roleId)

        // Проверка результата
        assert(result)

        // Проверка вызова мока
        verify(roleRepository).delete(roleId)
    }

    @Test
    fun `должен выбросить исключение при удалении роли с неположительным ID`() {
        // Проверка выброса исключения
        assertThrows<IllegalArgumentException> {
            roleService.delete(0)
        }

        // Проверка что мок не вызывался
        verify(roleRepository, never()).delete(any())
    }
}
