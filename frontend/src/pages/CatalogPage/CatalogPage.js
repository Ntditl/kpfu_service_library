import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./CatalogPage.css";
import axios from 'axios';
import { mockBooks } from '../../data/mockData'; 

function CatalogPage() {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isUsingMockData, setIsUsingMockData] = useState(false);

  const api = axios.create({
    baseURL: 'https://285f-178-208-64-74.ngrok-free.app',
    headers: {
      'ngrok-skip-browser-warning': 'true'
    },
    timeout: 5000
  });

  useEffect(() => {
    const fetchBooks = async () => {
      try {
        const response = await api.get('/books');
        setBooks(response.data);
        setIsUsingMockData(false);
      } catch (err) {
        console.error("Ошибка загрузки каталога, используем моковые данные:", err);
        const savedBooks = JSON.parse(localStorage.getItem('mockBooks'));
        setBooks(savedBooks || mockBooks);
        setIsUsingMockData(true);
        setError("Сервер недоступен. Показаны демонстрационные данные.");
      } finally {
        setLoading(false);
      }
    };

    fetchBooks();
  }, []);

  if (loading) return <div className="loading">Загрузка каталога...</div>;

  return (
    <div className="catalog-page">
      <h1>Каталог книг</h1>
      {isUsingMockData && (
        <div className="warning-banner">
          Сервер недоступен. Показаны демонстрационные данные.
        </div>
      )}
      {error && !isUsingMockData && <div className="error">{error}</div>}
      <div className="books-grid">
        {books.map(book => (
          <Link to={`/book/${book.bookId}`} key={book.bookId} className="book-card">
            <div className="book-cover">
              <img src={book.coverUrl} alt={`Обложка ${book.title}`} />
            </div>
            <div className="book-info">
              <h3>{book.title}</h3>
              <p>{book.author}</p>
            </div>
          </Link>
        ))}
      </div>
    </div>
  );
}

export default CatalogPage;