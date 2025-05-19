import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import BookCover from "../../components/BookCover/BookCover";
import BookInfo from "../../components/BookInfo/BookInfo";
import ActionButtons from "../../components/ActionButtons/ActionButtons";
import "./BookPage.css";
import axios from 'axios';
import { mockBooks } from '../../data/mockData';
import { api } from '../../api';

function BookPage() {
  const { bookId } = useParams();
  const [bookData, setBookData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isUsingMockData, setIsUsingMockData] = useState(false);

  const api = axios.create({
    baseURL: 'https://cbbd-178-208-64-74.ngrok-free.app',
    headers: {
      'ngrok-skip-browser-warning': 'true'
    },
    timeout: 5000
  });

  useEffect(() => {
    const fetchBookData = async () => {
      try {
        const response = await api.get('/books');
        const allBooks = response.data;
        const selectedBook = allBooks.find(book => book.bookId.toString() === bookId);
        
        if (!selectedBook) throw new Error("Книга не найдена");
        
        setBookData(formatBookData(selectedBook));
        setIsUsingMockData(false);
      } catch (err) {
        console.error("Ошибка запроса, используем моковые данные:", err);
        
        const savedBooks = JSON.parse(localStorage.getItem('mockBooks')) || mockBooks;
        const foundBook = savedBooks.find(book => book.bookId.toString() === bookId);
        
        if (foundBook) {
          setBookData(formatBookData(foundBook));
          setIsUsingMockData(true);
          setError("Сервер недоступен. Показаны демонстрационные данные.");
        } else {
          setError("Книга не найдена");
        }
      } finally {
        setLoading(false);
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
          <ActionButtons bookId={bookData.id} />
        </div>
        <div className="book-details">
          <BookInfo {...bookData} />
        </div>
      </div>
    </div>
  );
}

export default BookPage;