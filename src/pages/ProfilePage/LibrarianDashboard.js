import React, { useEffect, useState } from "react";
import "./LibrarianDashboard.css";
import { api } from "../../api";

function LibrarianDashboard() {
  const [requests, setRequests] = useState([]);

  useEffect(() => {
    const fetchAllRequests = async () => {
      try {
        const response = await api.get("/reservations");
        setRequests(response.data); // ожидание массива всех заявок
      } catch (error) {
        console.error("Ошибка при получении всех заявок:", error);
      }
    };

    fetchAllRequests();
  }, []);

  const renderStatus = (status) => {
    if (status === true) return "✅ Одобрено";
    if (status === false) return "❌ Отклонено";
    return "🕒 Ожидает";
  };

  return (
    <div className="librarian-dashboard">
      <h2>Заявки пользователей</h2>
      {requests.length === 0 ? (
        <p>Нет заявок.</p>
      ) : (
        <ul className="request-list">
          {requests.map((req) => (
            <li key={req.reservation_id} className="request-item">
              <p><strong>Пользователь:</strong> {req.user?.first_name} {req.user?.last_name}</p>
              <p><strong>Книга:</strong> {req.book?.title}</p>
              <p><strong>Дата запроса:</strong> {new Date(req.date_request_from_user).toLocaleString()}</p>
              <p><strong>Статус:</strong> {renderStatus(req.res_answer_to_user)}</p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default LibrarianDashboard;
