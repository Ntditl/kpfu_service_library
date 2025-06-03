import React from "react";
import UserDashboard from "./UserDashboard";
import LibrarianDashboard from "./LibrarianDashboard";
import AdminDashboard from "./AdminDashboard";
import "./ProfilePage.css";

const ProfilePage = () => {
  const roleId = localStorage.getItem("role_id");

  return (
    <div className="profile-page">
      <h1>Личный кабинет</h1>

      {roleId === "1" && <UserDashboard />}
      {roleId === "2" && <LibrarianDashboard />}
      {roleId === "3" && <AdminDashboard />}

      {!roleId && <p>Роль пользователя не определена</p>}
    </div>
  );
};

export default ProfilePage;