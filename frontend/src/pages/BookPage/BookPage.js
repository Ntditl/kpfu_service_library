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
    const fetchData = async () => {
      try {
        const bookRes = await api.get('/books');
        const allBooks = bookRes.data;
        const selected = allBooks.find(book => book.bookId.toString() === bookId);
        if (!selected) throw new Error("Книга не найдена");

        const formatted = formatBookData(selected);
        setBookData(formatted);
        setIsUsingMockData(false);

        await fetchBookCopies(selected.bookId);
      } catch (err) {
        console.warn("Ошибка запроса, переключаемся на мок:", err);
        const saved = JSON.parse(localStorage.getItem('mockBooks')) || mockBooks;
        const found = saved.find(book => book.bookId.toString() === bookId);

        if (found) {
          setBookData(formatBookData(found));
          setCopies([]); // мок-экземпляров нет
          setIsUsingMockData(true);
          setError("Сервер недоступен. Показаны демонстрационные данные.");
        } else {
          setError("Книга не найдена");
        }
      } finally {
        setLoading(false);
      }
    };

    const fetchBookCopies = async (bookId) => {
      try {
        const res = await api.get("/book-copies");
        const filtered = res.data.filter(c => c.book_id === Number(bookId));
        setCopies(filtered);
      } catch (err) {
        console.error("Ошибка загрузки экземпляров:", err);
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

    fetchData();
  }, [bookId]);

  const refreshCopies = async () => {
    if (isUsingMockData) return;
    try {
      const res = await api.get("/book-copies");
      const filtered = res.data.filter(c => c.book_id === Number(bookId));
      setCopies(filtered);
    } catch (err) {
      console.error("Ошибка обновления экземпляров:", err);
    }
  };

  if (loading) return <div className="loading">Загрузка...</div>;
  if (error && !isUsingMockData) return <div className="error">{error}</div>;
  if (!bookData) return <div className="error">Данные о книге не загружены</div>;

  const availableCount = copies.filter(c => c.is_in_here && !c.is_in_reservation).length;

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
            refreshCopies={refreshCopies}
          />
        </div>
        <div className="book-details">
          <BookInfo {...bookData} />
          <p><strong>Доступно экземпляров: </strong>{availableCount}</p>
        </div>
      </div>
    </div>
  );
}

export default BookPage;


