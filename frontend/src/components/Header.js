import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import "./Header.css";

function Header() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="header">
      <div className="logo">
        <img src="/images/logo.png" alt="Логотип" className="logo-img" />
      </div>
      <Link to="/" className="catalog-but">
        Каталог
      </Link>
      
      <div className="search-container">
        <input 
          type="text" 
          placeholder="Поиск..." 
          className="search-input"
        />
        <button className="search-button">
          <svg width="30" height="30" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M11 19C15.4183 19 19 15.4183 19 11C19 6.58172 15.4183 3 11 3C6.58172 3 3 6.58172 3 11C3 15.4183 6.58172 19 11 19Z" stroke="currentColor" strokeWidth="2"/>
            <path d="M21 21L16.65 16.65" stroke="currentColor" strokeWidth="2"/>
          </svg>
        </button>
      </div>
      <div className="notification-item">
        <img src="/images/icon-bell.png" alt="Уведомления" width="50" height="50"/>
        <span>Уведомления</span>
      </div>
      {user ? (
        <div className="user-menu">
          <div className="user-info">
            <img src="/images/profile_icon.png" alt="Профиль" width="50" height="50"/>
            <span>{user.first_name} {user.last_name}</span>
          </div>
          <button onClick={handleLogout} className="logout-button">
            Выйти
          </button>
        </div>
      ) : (
        <Link to="/login" className="login-item">
          <img src="/images/profile_icon.png" alt="Войти" width="50" height="50"/>
          <span>Войти</span>
        </Link>
      )}
    </header>
  );
}

export default Header;