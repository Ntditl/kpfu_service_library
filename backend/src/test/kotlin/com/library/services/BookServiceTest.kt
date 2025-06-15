package com.library.services

import com.library.models.BookDTO
import com.library.repositories.BookRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*

class BookServiceTest {

    private lateinit var bookRepository: BookRepository
    private lateinit var bookService: BookService

    @BeforeEach
    fun setup() {
        bookRepository = mock()
        bookService = BookService(bookRepository)
    }

    @Test
    fun `должен получить список всех книг`() {
        // Подготовка данных
        val books = listOf(
            BookDTO(bookId = 1, title = "Война и мир", author = "Лев Толстой", isbn = "123456789",
                publicationYear = 1869, publisher = "Русский вестник", genre = "Роман",
                description = "Описание книги", keywords = "война, мир", coverUrl = "url1"),
            BookDTO(bookId = 2, title = "Мастер и Маргарита", author = "Михаил Булгаков", isbn = "987654321",
                publicationYear = 1967, publisher = "Москва", genre = "Роман",
                description = "Описание книги", keywords = "москва, дьявол", coverUrl = "url2")
        )

        // Настройка поведения мока
        whenever(bookRepository.getAll()).thenReturn(books)

        // Вызов тестируемого метода
        val result = bookService.getAll()

        // Проверка результата
        assert(result.size == 2)
        assert(result[0].title == "Война и мир")
        assert(result[1].title == "Мастер и Маргарита")

        // Проверка вызова мока
        verify(bookRepository).getAll()
    }

    @Test
    fun `должен получить книгу по ID`() {
        // Подготовка данных
        val bookId = 1
        val book = BookDTO(
            bookId = bookId,
            title = "Война и мир",
            author = "Лев Толстой",
            isbn = "123456789",
            publicationYear = 1869,
            publisher = "Русский вестник",
            genre = "Роман",
            description = "Описание книги",
            keywords = "война, мир",
            coverUrl = "url1"
        )

        // Настройка поведения мока
        whenever(bookRepository.getById(bookId)).thenReturn(book)

        // Вызов тестируемого метода
        val result = bookService.getById(bookId)

        // Проверка результата
        assert(result != null)
        assert(result?.bookId == bookId)
        assert(result?.title == "Война и мир")

        // Проверка вызова мока
        verify(bookRepository).getById(bookId)
    }

    @Test
    fun `должен выбросить исключение при поиске книги с неположительным ID`() {
        // Проверка выброса исключения
        assertThrows<IllegalArgumentException> {
            bookService.getById(0)
        }

        // Проверка что мок не вызывался
        verify(bookRepository, never()).getById(any())
    }

    @Test
    fun `должен создать новую книгу`() {
        // Подготовка данных
        val bookDTO = BookDTO(
            title = "Война и мир",
            author = "Лев Толстой",
            isbn = "123456789",
            publicationYear = 1869,
            publisher = "Русский вестник",
            genre = "Роман",
            description = "Описание книги",
            keywords = "война, мир",
            coverUrl = "url1"
        )
        val createdBookId = 1

        // Настройка поведения мока
        whenever(bookRepository.create(bookDTO)).thenReturn(createdBookId)

        // Вызов тестируемого метода
        val result = bookService.create(bookDTO)

        // Проверка результата
        assert(result == createdBookId)

        // Проверка вызова мока
        verify(bookRepository).create(bookDTO)
    }

    @Test
    fun `должен выбросить исключение при создании книги с пустым заголовком`() {
        // Подготовка данных
        val bookDTO = BookDTO(
            title = "",  // Пустой заголовок
            author = "Автор",
            isbn = null,
            publicationYear = null,
            publisher = null,
            genre = null,
            description = null,
            keywords = null,
            coverUrl = null
        )

        // Проверка выброса исключения
        assertThrows<IllegalArgumentException> {
            bookService.create(bookDTO)
        }

        // Проверка что мок не вызывался
        verify(bookRepository, never()).create(any())
    }

    @Test
    fun `должен обновить существующую книгу`() {
        // Подготовка данных
        val bookId = 1
        val bookDTO = BookDTO(
            title = "Война и мир (обновлено)",
            author = "Лев Толстой",
            isbn = "123456789",
            publicationYear = 1869,
            publisher = "Русский вестник",
            genre = "Роман",
            description = "Обновленное описание",
            keywords = "война, мир, роман",
            coverUrl = "url1_updated"
        )

        // Настройка поведения мока
        whenever(bookRepository.update(bookId, bookDTO)).thenReturn(true)

        // Вызов тестируемого метода
        val result = bookService.update(bookId, bookDTO)

        // Проверка результата
        assert(result)

        // Проверка вызова мока
        verify(bookRepository).update(bookId, bookDTO)
    }

    @Test
    fun `должен удалить книгу по ID`() {
        // Подготовка данных
        val bookId = 1

        // Настройка поведения мока
        whenever(bookRepository.delete(bookId)).thenReturn(true)

        // Вызов тестируемого метода
        val result = bookService.delete(bookId)

        // Проверка результата
        assert(result)

        // Проверка вызова мока
        verify(bookRepository).delete(bookId)
    }

    @Test
    fun `должен выбросить исключение при удалении книги с неположительным ID`() {
        // Проверка выброса исключения
        assertThrows<IllegalArgumentException> {
            bookService.delete(0)
        }

        // Проверка что мок не вызывался
        verify(bookRepository, never()).delete(any())
    }
}
