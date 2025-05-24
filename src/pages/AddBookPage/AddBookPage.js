import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { mockBooks } from '../../data/mockData';
import { api } from '../../api';
import './AddBookPage.css';

const AddBookPage = () => {
  const [formData, setFormData] = useState({
    title: '',
    author: '',
    isbn: '',
    publicationYear: '',
    publisher: '',
    genre: '',
    description: '',
    keywords: '',
    coverUrl: ''
  });
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [isUsingMockData, setIsUsingMockData] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    
    try {
      try {
        const response = await api.post('/books', formData);
        if (response.status === 201) {
          if (isUsingMockData) {
            const savedBooks = JSON.parse(localStorage.getItem('mockBooks')) || [];
            localStorage.setItem(
              'mockBooks', 
              JSON.stringify(savedBooks.filter(b => b.isbn !== formData.isbn))
            );
          }
          navigate('/');
          return;
        }
      } catch (apiError) {
        if (apiError.response) {
          setError(apiError.response.data.message || 'Ошибка сервера');
          return;
        } else {
          setIsUsingMockData(true);
        }
      }
      
      const savedBooks = JSON.parse(localStorage.getItem('mockBooks')) || [];
      const newBook = {
        bookId: Math.max(...savedBooks.map(b => b.bookId), 0) + 1,
        ...formData,
        publicationYear: parseInt(formData.publicationYear)
      };
      
      localStorage.setItem('mockBooks', JSON.stringify([...savedBooks, newBook]));
      navigate('/');
      
    } catch (err) {
      console.error('Ошибка:', err);
      setError('Не удалось добавить книгу');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="add-book-page">
      <h2>Добавить новую книгу</h2>
      {isUsingMockData && (
        <div className="warning-banner">
          Сервер недоступен. Книга сохранена локально.
        </div>
      )}
      {error && <div className="error-message">{error}</div>}
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Название:</label>
          <input
            type="text"
            name="title"
            value={formData.title}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Автор:</label>
          <input
            type="text"
            name="author"
            value={formData.author}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>ISBN:</label>
          <input
            type="text"
            name="isbn"
            value={formData.isbn}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Год издания:</label>
          <input
            type="number"
            name="publicationYear"
            value={formData.publicationYear}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Издательство:</label>
          <input
            type="text"
            name="publisher"
            value={formData.publisher}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Жанр:</label>
          <input
            type="text"
            name="genre"
            value={formData.genre}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Описание:</label>
          <textarea
            name="description"
            value={formData.description}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Ключевые слова (через запятую):</label>
          <input
            type="text"
            name="keywords"
            value={formData.keywords}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label>Ссылка на обложку:</label>
          <input
            type="url"
            name="coverUrl"
            value={formData.coverUrl}
            onChange={handleChange}
            required
          />
          {formData.coverUrl && (
            <div className="cover-preview">
              <img src={formData.coverUrl} alt="Превью обложки" />
            </div>
          )}
        </div>

        <button type="submit" disabled={loading}>
          {loading ? 'Добавление...' : 'Добавить книгу'}
        </button>
      </form>
    </div>
  );
};



export default AddBookPage;