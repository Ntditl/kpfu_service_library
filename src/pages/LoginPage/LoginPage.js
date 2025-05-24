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

    try {
      const response = await api.post('/auth/login', formData);
      const user = response.data.user;

      // Сохраняем всё, что нужно
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user_id', user.user_id); // проверь, как именно называется id в бэкенде
      localStorage.setItem('first_name', user.first_name);
      localStorage.setItem('last_name', user.last_name);
      localStorage.setItem('phone', user.phone);
      localStorage.setItem('role', user.role);

      // Редирект в зависимости от роли
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
      setError('Неверный email или пароль');
    }
  };

  return (
    <div className="auth-page">
      <h2>Вход</h2>
      {error && <div className="error-message">{error}</div>}
      <form onSubmit={handleSubmit}>
        <input type="email" name="email" placeholder="Email" required onChange={handleChange} />
        <input type="password" name="password" placeholder="Пароль" required onChange={handleChange} />
        <button type="submit">Войти</button>
      </form>
      <p>Нет аккаунта? <a href="/register">Зарегистрироваться</a></p>
    </div>
  );
}

export default LoginPage;

