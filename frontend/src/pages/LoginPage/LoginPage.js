import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './LoginPage.css';
import { api } from '../../api';

function LoginPage() {
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    console.log('Отправка формы с данными:', formData);

    try {
      console.log('Отправка запроса на:', '/auth/login');
      const response = await api.post('/auth/login', formData);
      console.log('Весь ответ от сервера:', response.data);
      const user = response.data.user;
      console.log('Данные пользователя:', user);

      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user_id', user.user_id);
      localStorage.setItem('first_name', user.first_name);
      localStorage.setItem('last_name', user.last_name);
      localStorage.setItem('phone', user.phone);
      localStorage.setItem('role', user.role);
      localStorage.setItem('role_id', user.role_id);

      console.log('Сохраненные значения:', {
        role: localStorage.getItem('role'),
        user_id: localStorage.getItem('user_id'),
        first_name: localStorage.getItem('first_name')
      });

      if (user.role === 'user') {
        navigate('/profile/user');
      } else if (user.role === 'librarian') {
        navigate('/profile/librarian');
      } else if (user.role === 'admin') {
        navigate('/profile/admin');
      } else {
        navigate('/');
      }

    } catch (err) {
      console.error("Ошибка входа:", err);
      if (err.response) {
        console.error("Ответ сервера при ошибке:", err.response.data);
        console.error("Статус ошибки:", err.response.status);
        setError(err.response.data.message || 'Неверный email или пароль');
      } else if (err.request) {
        console.error("Нет ответа от сервера:", err.request);
        setError('Нет ответа от сервера. Проверьте подключение к интернету.');
      } else {
        console.error("Ошибка запроса:", err.message);
        setError('Произошла ошибка при входе. Попробуйте позже.');
      }
    }
  };

  return (
    <div className="auth-page">
      <h2>Вход</h2>
      {error && <div className="error-message">{error}</div>}
      <form onSubmit={handleSubmit}>
        <input 
          type="email" 
          name="email" 
          placeholder="Email" 
          required 
          onChange={handleChange}
          value={formData.email}
        />
        <input 
          type="password" 
          name="password" 
          placeholder="Пароль" 
          required 
          onChange={handleChange}
          value={formData.password}
        />
        <button type="submit">Войти</button>
      </form>
      <p>Нет аккаунта? <a href="/register">Зарегистрироваться</a></p>
    </div>
  );
}

export default LoginPage;