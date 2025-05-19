import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { signInWithEmailAndPassword } from 'firebase/auth';
import { auth } from '../../../firebase';
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
      // Входим через Firebase
      const userCredential = await signInWithEmailAndPassword(auth, formData.email, formData.password);

      // Получаем данные пользователя с бэкенда
      const response = await api.get(`/users/${userCredential.user.uid}`);

      localStorage.setItem('user', JSON.stringify(response.data));
      navigate('/');
    } catch (err) {
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
