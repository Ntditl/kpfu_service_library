import React, { useEffect, useState } from "react";
import "./AdminDashboard.css";
import { api } from "../../api";

function AdminDashboard() {
  const [users, setUsers] = useState([]);
  const [roleRequests, setRoleRequests] = useState([]);
  const [showRoleRequests, setShowRoleRequests] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [usersRes, roleRequestsRes] = await Promise.all([
          api.get("/users"),
          api.get("/borrowings-to-role")
        ]);
        setUsers(usersRes.data);
        setRoleRequests(roleRequestsRes.data);
      } catch (error) {
        console.error("Ошибка при загрузке данных:", error);
      }
    };

    fetchData();
  }, []);

  const getUserName = (userId) => {
    const user = users.find(u => u.user_id === userId);
    return user ? `${user.first_name} ${user.last_name}` : "Неизвестный пользователь";
  };

  const handleRoleDecision = async (roleReservationId, decision) => {
    try {
      const request = roleRequests.find(r => r.role_reservation_id === roleReservationId);
      if (!request) {
        console.error("Заявка не найдена");
        return;
      }

      if (decision === true) {
        await api.put(`/users/${request.user_id}`, {
          role_id: 2 // Предполагаем, что role_id=2 соответствует роли библиотекаря
        });
        localStorage.setItem("role", "librarian");
      }

      await api.put(`/borrowings-to-role/${roleReservationId}`, {
        res_answer_to_request: decision,
        date_answer_to_request: new Date().toISOString().slice(0, 16),
      });

      setRoleRequests(prev =>
        prev.map(r =>
          r.role_reservation_id === roleReservationId
            ? { 
                ...r, 
                res_answer_to_request: decision, 
                date_answer_to_request: new Date().toISOString() 
              }
            : r
        )
      );

      if (decision === true) {
        const updatedUser = users.find(u => u.user_id === request.user_id);
        if (updatedUser) {
          setUsers(prev =>
            prev.map(u =>
              u.user_id === request.user_id
                ? { ...u, role_id: 2 }
                : u
            )
          );
        }
      }

    } catch (error) {
      console.error("Ошибка при обработке решения:", error);
      alert("Произошла ошибка при обработке решения. Пожалуйста, попробуйте снова.");
    }
  };

  const renderRoleStatus = (status) => {
    if (status === true) return "✅ Одобрено";
    if (status === false) return "❌ Отклонено";
    return "🕒 Ожидает";
  };

  return (
    <div className="admin-dashboard">
      <h2>Панель администратора</h2>

      <div className="role-requests-section">
        <h3>Заявки на роль библиотекаря</h3>
        <button
          className="toggle-button"
          onClick={() => setShowRoleRequests(!showRoleRequests)}
        >
          {showRoleRequests ? "Скрыть заявки на роли" : "Показать заявки на роли"}
        </button>

        {showRoleRequests && (
          roleRequests.length === 0 ? (
            <p>Нет заявок на роль библиотекаря.</p>
          ) : (
            <ul className="role-request-list">
              {roleRequests.map((req) => {
                const statusClass = req.res_answer_to_request === true
                  ? "approved"
                  : req.res_answer_to_request === false
                  ? "rejected"
                  : "pending";

                return (
                  <li key={req.role_reservation_id} className={`role-request-item ${statusClass}`}>
                    <p><strong>Пользователь:</strong> {getUserName(req.user_id)}</p>
                    <p><strong>Дата заявки:</strong> {new Date(req.date_request_from).toLocaleString()}</p>
                    <p><strong>Статус:</strong> {renderRoleStatus(req.res_answer_to_request)}</p>
                    {req.res_answer_to_request === null && (
                      <div className="decision-buttons">
                        <button onClick={() => handleRoleDecision(req.role_reservation_id, true)}>Одобрить</button>
                        <button onClick={() => handleRoleDecision(req.role_reservation_id, false)}>Отклонить</button>
                      </div>
                    )}
                  </li>
                );
              })}
            </ul>
          )
        )}
      </div>
    </div>
  );
}

export default AdminDashboard;