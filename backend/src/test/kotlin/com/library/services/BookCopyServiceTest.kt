package com.library.services

import com.library.models.*
import com.library.repositories.BookCopyRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*

class BookCopyServiceTest {

    private lateinit var bookCopyRepository: BookCopyRepository
    private lateinit var bookCopyService: BookCopyService

    @BeforeEach
    fun setup() {
        bookCopyRepository = mock()
        bookCopyService = BookCopyService(bookCopyRepository)
    }

    @Test
    fun `должен получить список всех экземпляров книг`() {
        // Подготовка данных
        val bookCopies = listOf(
            BookCopyDTO(copy_id = 1, book_id = 1, is_in_here = true, is_in_reservation = false, borrowed_By_Other_User = false),
            BookCopyDTO(copy_id = 2, book_id = 1, is_in_here = true, is_in_reservation = true, borrowed_By_Other_User = false),
            BookCopyDTO(copy_id = 3, book_id = 2, is_in_here = false, is_in_reservation = false, borrowed_By_Other_User = true)
        )

        // Настройка поведения мока
        whenever(bookCopyRepository.getAll()).thenReturn(bookCopies)

        // Вызов тестируемого метода
        val result = bookCopyService.getAll()

        // Проверка результата
        assert(result.size == 3)
        assert(result[0].copy_id == 1)
        assert(result[0].book_id == 1)
        assert(result[0].is_in_here)
        assert(!result[0].is_in_reservation)

        // Проверка вызова мока
        verify(bookCopyRepository).getAll()
    }

    @Test
    fun `должен получить экземпляр книги по ID`() {
        // Подготовка данных
        val copyId = 1
        val bookCopy = BookCopyDTO(
            copy_id = copyId,
            book_id = 1,
            is_in_here = true,
            is_in_reservation = false,
            borrowed_By_Other_User = false
        )

        // Настройка поведения мока
        whenever(bookCopyRepository.getById(copyId)).thenReturn(bookCopy)

        // Вызов тестируемого метода
        val result = bookCopyService.getById(copyId)

        // Проверка результата
        assert(result != null)
        assert(result?.copy_id == copyId)
        assert(result?.book_id == 1)
        assert(result?.is_in_here == true)

        // Проверка вызова мока
        verify(bookCopyRepository).getById(copyId)
    }

    @Test
    fun `должен выбросить исключение при поиске экземпляра книги с неположительным ID`() {
        // Проверка выброса исключения
        assertThrows<IllegalArgumentException> {
            bookCopyService.getById(0)
        }

        // Проверка что мок не вызывался
        verify(bookCopyRepository, never()).getById(any())
    }

    @Test
    fun `должен создать новый экземпляр книги`() {
        // Подготовка данных
        val bookCopyDTO = BookCopyDTO(
            book_id = 1,
            is_in_here = true,
            is_in_reservation = false,
            borrowed_By_Other_User = false
        )
        val createdCopyId = 1

        // Настройка поведения мока
        whenever(bookCopyRepository.create(bookCopyDTO)).thenReturn(createdCopyId)

        // Вызов тестируемого метода
        val result = bookCopyService.create(bookCopyDTO)

        // Проверка результата
        assert(result == createdCopyId)

        // Проверка вызова мока
        verify(bookCopyRepository).create(bookCopyDTO)
    }

    @Test
    fun `должен выбросить исключение при создании экземпляра книги с неположительным ID книги`() {
        // Подготовка данных
        val bookCopyDTO = BookCopyDTO(
            book_id = 0,  // Неположительный ID книги
            is_in_here = true,
            is_in_reservation = false,
            borrowed_By_Other_User = false
        )

        // Проверка выброса исключения
        assertThrows<IllegalArgumentException> {
            bookCopyService.create(bookCopyDTO)
        }

        // Проверка что мок не вызывался
        verify(bookCopyRepository, never()).create(any())
    }

    @Test
    fun `должен обновить существующий экземпляр книги`() {
        // Подготовка данных
        val copyId = 1
        val bookCopyDTO = BookCopyDTO(
            book_id = 1,
            is_in_here = false,
            is_in_reservation = true,
            borrowed_By_Other_User = true
        )

        // Настройка поведения мока
        whenever(bookCopyRepository.update(copyId, bookCopyDTO)).thenReturn(true)

        // Вызов тестируемого метода
        val result = bookCopyService.update(copyId, bookCopyDTO)

        // Проверка результата
        assert(result)

        // Проверка вызова мока
        verify(bookCopyRepository).update(copyId, bookCopyDTO)
    }

    @Test
    fun `должен удалить экземпляр книги по ID`() {
        // Подготовка данных
        val copyId = 1

        // Настройка поведения мока
        whenever(bookCopyRepository.delete(copyId)).thenReturn(true)

        // Вызов тестируемого метода
        val result = bookCopyService.delete(copyId)

        // Проверка результата
        assert(result)

        // Проверка вызова мока
        verify(bookCopyRepository).delete(copyId)
    }

    @Test
    fun `должен обновить статус бронирования экземпляра книги`() {
        // Подготовка данных
        val copyId = 1
        val updateDTO = UpdateBookCopyReservationDTO(is_in_reservation = true)

        // Настройка поведения мока
        whenever(bookCopyRepository.updateReservation(copyId, updateDTO.is_in_reservation)).thenReturn(true)

        // Вызов тестируемого метода
        val result = bookCopyService.updateReservation(copyId, updateDTO)

        // Проверка результата
        assert(result)

        // Проверка вызова мока
        verify(bookCopyRepository).updateReservation(copyId, updateDTO.is_in_reservation)
    }

    @Test
    fun `должен обновить местоположение экземпляра книги`() {
        // Подготовка данных
        val copyId = 1
        val updateDTO = UpdateBookCopyLocationDTO(is_in_here = false)

        // Настройка поведения мока
        whenever(bookCopyRepository.updateLocation(copyId, updateDTO.is_in_here)).thenReturn(true)

        // Вызов тестируемого метода
        val result = bookCopyService.updateLocation(copyId, updateDTO)

        // Проверка результата
        assert(result)

        // Проверка вызова мока
        verify(bookCopyRepository).updateLocation(copyId, updateDTO.is_in_here)
    }

    @Test
    fun `должен обновить статус выдачи экземпляра книги`() {
        // Подготовка данных
        val copyId = 1
        val updateDTO = UpdateBookCopyBorrowedStatusDTO(borrowed_By_Other_User = true)

        // Настройка поведения мока
        whenever(bookCopyRepository.updateBorrowedStatus(copyId, updateDTO.borrowed_By_Other_User)).thenReturn(true)

        // Вызов тестируемого метода
        val result = bookCopyService.updateBorrowedStatus(copyId, updateDTO)

        // Проверка результата
        assert(result)

        // Проверка вызова мока
        verify(bookCopyRepository).updateBorrowedStatus(copyId, updateDTO.borrowed_By_Other_User)
    }
}
