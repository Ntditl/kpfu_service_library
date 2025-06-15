package com.library.services

import com.library.models.LoginRequest
import com.library.models.RegisterRequest
import com.library.models.UserDTO
import com.library.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AuthServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var jwtService: JwtService
    private lateinit var authService: AuthService

    @BeforeEach
    fun setup() {
        userRepository = mock()
        jwtService = mock()
        authService = AuthService(userRepository, jwtService)
    }

    @Test
    fun `должен успешно зарегистрировать нового пользователя`() {
        // Подготовка данных для теста
        val request = RegisterRequest(
            email = "test@example.com",
            password = "password123",
            first_name = "Иван",
            last_name = "Петров",
            phone = "+79001234567"
        )

        val userId = 1
        val mockUser = UserDTO(
            user_id = userId,
            email = request.email,
            first_name = request.first_name,
            last_name = request.last_name,
            phone = request.phone,
            role_id = 1,
            is_active = true
        )

        val mockToken = "jwt.token.example"

        // Настройка поведения моков
        whenever(userRepository.findByEmail(request.email)).thenReturn(null)
        whenever(userRepository.create(any(), any())).thenReturn(userId)
        whenever(userRepository.findById(userId)).thenReturn(mockUser)
        whenever(jwtService.generateToken(mockUser)).thenReturn(mockToken)

        // Вызов тестируемого метода
        val response = authService.register(request)

        // Проверка результата
        assert(response.token == mockToken)
        assert(response.user == mockUser)

        // Проверка вызовов методов моков
        verify(userRepository).findByEmail(request.email)
        verify(userRepository).create(any(), any())
        verify(userRepository).findById(userId)
        verify(jwtService).generateToken(mockUser)
    }

    @Test
    fun `должен выбрасывать исключение при регистрации с существующим email`() {
        // Подготовка данных для теста
        val request = RegisterRequest(
            email = "existing@example.com",
            password = "password123",
            first_name = "Иван",
            last_name = "Петров",
            phone = "+79001234567"
        )

        val existingUser = UserDTO(
            user_id = 1,
            email = request.email,
            first_name = "Существующий",
            last_name = "Пользователь",
            phone = "+79009876543",
            role_id = 1,
            is_active = true
        )

        // Настройка поведения моков
        whenever(userRepository.findByEmail(request.email)).thenReturn(existingUser)

        // Проверка выброса исключения
        val exception = assertThrows<IllegalArgumentException> {
            authService.register(request)
        }

        // Проверка сообщения об ошибке
        assert(exception.message == "Пользователь с таким email уже существует")

        // Проверка вызовов методов моков
        verify(userRepository).findByEmail(request.email)
        verify(userRepository, never()).create(any(), any())
    }
}
