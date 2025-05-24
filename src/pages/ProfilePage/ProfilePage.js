import React from "react";
import UserDashboard from "./UserDashboard";
import LibrarianDashboard from "./LibrarianDashboard";
import AdminDashboard from "./AdminDashboard";
import "./ProfilePage.css";

const ProfilePage = () => {
  const role = localStorage.getItem("role");

  return (
    <div className="profile-page">
      <h1>Личный кабинет</h1>

      {role === "user" && <UserDashboard />}
      {role === "librarian" && <LibrarianDashboard />}
      {role === "admin" && <AdminDashboard />}

      {!role && <p>Роль пользователя не определена</p>}
    </div>
  );
};

export default ProfilePage;
