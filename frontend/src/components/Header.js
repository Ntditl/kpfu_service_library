import { Link, useNavigate } from "react-router-dom";
import "./Header.css";
import React from "react";

function Header() {
  const navigate = useNavigate();
  const firstName = localStorage.getItem("first_name");
  const roleId = localStorage.getItem("role_id");
  const role = localStorage.getItem("role");

  // Проверяем все значения в localStorage
  console.log('Все значения из localStorage:', {
    firstName,
    roleId,
    role,
    userId: localStorage.getItem("user_id"),
    lastName: localStorage.getItem("last_name"),
    phone: localStorage.getItem("phone"),
    token: localStorage.getItem("token")
  });

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("first_name");
    localStorage.removeItem("last_name");
    localStorage.removeItem("phone");
    localStorage.removeItem("user_id");
    localStorage.removeItem("role_id");
    navigate("/");
    window.location.reload();
  };

  const getProfilePath = () => {
    console.log('getProfilePath - roleId:', roleId);
    switch (roleId) {
      case "1":
        return "/profile/user";
      case "2":
        return "/profile/librarian";
      case "3":
        return "/profile/admin";
      default:
        console.log('getProfilePath - default case, redirecting to login');
        return "/login";
    }
  };

  const handleProfileClick = () => {
    console.log('handleProfileClick вызван');
    console.log('Текущие значения в localStorage:', {
      firstName: localStorage.getItem('first_name'),
      roleId: localStorage.getItem('role_id'),
      role: localStorage.getItem('role')
    });
    
    if (!firstName) {
      console.log('firstName отсутствует, перенаправление на /login');
      navigate("/login");
      return;
    }
    const path = getProfilePath();
    console.log('Перенаправление на путь:', path);
    navigate(path);
  };

  return (
    <header className="header">
      <div className="logo">
        <img src="/images/logo.png" alt="Логотип" className="logo-img" />
      </div>
      <Link to="/" className="catalog-but">
        Каталог
      </Link>
      {firstName && (
        <Link to={getProfilePath()} className="catalog-but">
          Личный кабинет
        </Link>
      )}
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
      {firstName ? (
        <div className="login-wrapper">
          <div 
            className="profile-menu"
            onClick={handleProfileClick}
            style={{ cursor: "pointer" }}
          >
            <img src="/images/profile_icon.png" alt="Профиль" width="50" height="50" />
            <span>{firstName}</span>
          </div>
          <div className="logout-wrapper">
            <button onClick={handleLogout} className="logout-button">Выйти</button>
          </div>
        </div>
      ) : (
        <Link to="/login" className="login-item">
          <img src="/images/profile_icon.png" alt="Войти" width="50" height="50" />
          <span>Войти</span>
        </Link>
      )}
    </header>
  );
}

export default Header;