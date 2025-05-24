import React, { useEffect, useState } from "react";
import "./UserDashboard.css";
import { api } from "../../api";

function UserDashboard() {
  const user = {
    first_name: localStorage.getItem("first_name"),
    last_name: localStorage.getItem("last_name"),
    phone: localStorage.getItem("phone"),
    user_id: localStorage.getItem("user_id"),
  };

  const [requests, setRequests] = useState([]);

  useEffect(() => {
    const fetchRequests = async () => {
      try {
        const response = await api.get(`/reservations?user_id=${user.user_id}`);
        setRequests(response.data); // ожидается, что backend отдаёт массив заявок
      } catch (error) {
        console.error("Ошибка при получении заявок:", error);
      }
    };

    fetchRequests();
  }, [user.user_id]);

  const renderStatus = (status) => {
    if (status === true) return "Одобрено";
    if (status === false) return "Отклонено";
    return "Ожидает";
  };

  return (
    <div className="user-dashboard">
      <h2>Личный кабинет</h2>
      <div className="user-info">
        <p><strong>Имя:</strong> {user.first_name}</p>
        <p><strong>Фамилия:</strong> {user.last_name}</p>
        <p><strong>Телефон:</strong> {user.phone}</p>
      </div>

      <div className="requests-section">
        <h3>Мои заявки</h3>
        {requests.length === 0 ? (
          <p>У вас пока нет заявок.</p>
        ) : (
          <ul className="request-list">
            {requests.map((req) => (
              <li key={req.reservation_id} className="request-item">
                <p><strong>Книга:</strong> {req.book?.title || "Неизвестно"}</p>
                <p><strong>Дата заявки:</strong> {new Date(req.date_request_from_user).toLocaleString()}</p>
                <p><strong>Статус:</strong> {renderStatus(req.res_answer_to_user)}</p>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}

export default UserDashboard;
