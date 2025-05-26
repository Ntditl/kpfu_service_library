import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import BookCover from "../../components/BookCover/BookCover";
import BookInfo from "../../components/BookInfo/BookInfo";
import ActionButtons from "../../components/ActionButtons/ActionButtons";
import "./BookPage.css";
import { mockBooks } from '../../data/mockData';
import { api } from '../../api';

function BookPage() {
  const { bookId } = useParams();
  const [bookData, setBookData] = useState(null);
  const [copies, setCopies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isUsingMockData, setIsUsingMockData] = useState(false);

  useEffect(() => {
    const fetchBookData = async () => {
      try {
        const response = await api.get('/books');
        const allBooks = response.data;
        const selectedBook = allBooks.find(book => book.bookId.toString() === bookId);

        if (!selectedBook) throw new Error("Книга не найдена");

        setBookData(formatBookData(selectedBook));
        setIsUsingMockData(false);

        await fetchBookCopies(selectedBook.bookId);
      } catch (err) {
        console.error("Ошибка запроса, используем моковые данные:", err);

        const savedBooks = JSON.parse(localStorage.getItem('mockBooks')) || mockBooks;
        const foundBook = savedBooks.find(book => book.bookId.toString() === bookId);

        if (foundBook) {
          setBookData(formatBookData(foundBook));
          setIsUsingMockData(true);
          setError("Сервер недоступен. Показаны демонстрационные данные.");
          setCopies([]); // мок-копий нет
        } else {
          setError("Книга не найдена");
        }
      } finally {
        setLoading(false);
      }
    };

    const fetchBookCopies = async (bookId) => {
    try {
        const res = await api.get("/book-copies"); // получаем все
        const allCopies = res.data;
        const filtered = allCopies.filter(copy => copy.book_id === Number(bookId));
        setCopies(filtered);
      } catch (err) {
        console.error("Ошибка загрузки экземпляров книги:", err);
        setCopies([]);
      }
    };


    const formatBookData = (book) => ({
      id: book.bookId,
      title: book.title,
      author: book.author,
      coverImage: book.coverUrl,
      year: book.publicationYear,
      publish: book.publisher,
      isbn: book.isbn,
      genre: book.genre,
      description: book.description,
      keywords: book.keywords
    });

    fetchBookData();
  }, [bookId]);

  if (loading) return <div className="loading">Загрузка...</div>;
  if (error && !isUsingMockData) return <div className="error">{error}</div>;
  if (!bookData) return <div className="error">Данные о книге не загружены</div>;

  const availableCopies = copies.filter(c => c.is_in_here && !c.is_in_reservation).length;

  return (
    <div className="book-page">
      {isUsingMockData && (
        <div className="warning-banner">
          Сервер недоступен. Показаны демонстрационные данные.
        </div>
      )}
      <div className="book-main">
        <div className="book-but">
          <BookCover image={bookData.coverImage} />
          <ActionButtons
            bookId={bookData.id}
            copies={copies}
            refreshCopies={() => {
              if (!isUsingMockData) {
                api.get(`/book-copies?book_id=${bookId}`).then(res => setCopies(res.data));
              }
            }}
          />
        </div>
        <div className="book-details">
          <BookInfo {...bookData} />
          <p><strong>Доступно экземпляров: </strong>{availableCopies}</p>
        </div>
      </div>
    </div>
  );
}

export default BookPage;

