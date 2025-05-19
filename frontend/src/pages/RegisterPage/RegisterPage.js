import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import './RegisterPage.css';

function RegisterPage() {
  const [formData, setFormData] = useState({
    first_name: '',
    last_name: '',
    email: '',
    password: '',
    phone: ''
  });

  const [error, setError] = useState('');
  const navigate = useNavigate();
  const { register } = useAuth();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    
    const result = await register(formData);
    if (result.success) {
      navigate('/');
    } else {
      setError(result.error);
    }
  };

  return (
    <div className="auth-page">
      <h2>Регистрация</h2>

      {error && <div className="error-message">{error}</div>}

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="first_name"
          placeholder="Имя"
          required
          value={formData.first_name}
          onChange={handleChange}
        />

        <input
          type="text"
          name="last_name"
          placeholder="Фамилия"
          required
          value={formData.last_name}
          onChange={handleChange}
        />

        <input
          type="email"
          name="email"
          placeholder="Email"
          required
          value={formData.email}
          onChange={handleChange}
        />

        <input
          type="password"
          name="password"
          placeholder="Пароль"
          required
          value={formData.password}
          onChange={handleChange}
        />

        <input
          type="tel"
          name="phone"
          placeholder="Телефон"
          required
          value={formData.phone}
          onChange={handleChange}
        />

        <button type="submit">Зарегистрироваться</button>
      </form>

      <p>
        Уже есть аккаунт? <a href="/login">Войти</a>
      </p>
    </div>
  );
}

export default RegisterPage;

