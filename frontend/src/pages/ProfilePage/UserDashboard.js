import React, { useEffect, useState } from "react";
import "./UserDashboard.css";
import { api } from "../../api";

function UserDashboard() {
  const user = {
    first_name: localStorage.getItem("first_name"),
    last_name: localStorage.getItem("last_name"),
    phone: localStorage.getItem("phone"),
    user_id: localStorage.getItem("user_id"),
    role: localStorage.getItem("role"),
  };

  const [requests, setRequests] = useState([]);
  const [bookCopies, setBookCopies] = useState([]);
  const [books, setBooks] = useState([]);
  const [showRequests, setShowRequests] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [roleRequests, setRoleRequests] = useState([]);
  const [roleRequestStatus, setRoleRequestStatus] = useState(null);

  const fetchData = async () => {
    try {
      setLoading(true);
      setError(null);

      const [borrowingsRes, roleRequestsRes] = await Promise.all([
        api.get(`/borrowings?user_id=${user.user_id}`),
        api.get(`/borrowings-to-role?user_id=${user.user_id}`).catch((err) => {
          if (err.response?.status === 404) return { data: [] };
          throw err;
        }),
      ]);

      const userRequests = Array.isArray(borrowingsRes.data)
        ? borrowingsRes.data.filter(
            (req) => req.user_id.toString() === user.user_id
          )
        : [];

      if (userRequests.length > 0) {
        const [copiesRes, booksRes] = await Promise.all([
          api.get("/book-copies"),
          api.get("/books"),
        ]);
        setBookCopies(copiesRes.data);
        setBooks(booksRes.data);
      }

      const userRoleRequests = Array.isArray(roleRequestsRes.data)
        ? roleRequestsRes.data.filter(
            (req) => req.user_id.toString() === user.user_id
          )
        : [];

      setRoleRequests(userRoleRequests);

      const pending = userRoleRequests.find(
        (req) => req.res_answer_to_request === null
      );
      const approved = userRoleRequests.find(
        (req) => req.res_answer_to_request === true
      );
      const rejected = userRoleRequests.find(
        (req) => req.res_answer_to_request === false
      );

      setRoleRequestStatus(
        pending ? "pending" : approved ? "approved" : rejected ? "rejected" : null
      );

      setRequests(userRequests);
    } catch (error) {
      console.error("Ошибка при загрузке данных:", error);
      setError("Не удалось загрузить данные. Пожалуйста, проверьте подключение к серверу.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (user.user_id) {
      fetchData();
    } else {
      setError("Пользователь не авторизован");
      setLoading(false);
    }
  }, [user.user_id]);

  useEffect(() => {
    const interval = setInterval(() => {
      if (user.user_id) fetchData();
    }, 10000);
    return () => clearInterval(interval);
  }, [user.user_id]);

  const getBookTitleByCopyId = (copyId) => {
    const copy = bookCopies.find((c) => c.copy_id === copyId);
    const book = books.find((b) => b.bookId === copy?.book_id);
    return book?.title || "Неизвестно";
  };

  const handleRoleRequest = async () => {
    try {
      const response = await api.post("/borrowings-to-role", {
        user_id: user.user_id,
        role_id: 2,
        date_request_from: new Date().toISOString().slice(0, 16),
      });
      setRoleRequests((prev) => [
        ...prev,
        {
          role_reservation_id: response.data.id,
          user_id: user.user_id,
          role_id: 2,
          date_request_from: new Date().toISOString().slice(0, 16),
          res_answer_to_request: null,
        },
      ]);
      setRoleRequestStatus("pending");
      alert("Заявка на роль библиотекаря успешно подана!");
    } catch (error) {
      console.error("Ошибка при подаче заявки на роль:", error);
      alert("Произошла ошибка при подаче заявки. Пожалуйста, попробуйте снова.");
    }
  };

  const renderStatus = (status, date_of_start_of_issuance, date_to_return) => {
    if (date_to_return) return "📚 Книга сдана";
    if (status === true && date_of_start_of_issuance) return "📖 Книга выдана";
    if (status === true) return "✅ Одобрено, приходите";
    if (status === false)
      return "❌ Отклонено, обращаться по номеру: (843) 222-82-85";
    return "🕒 Ожидает";
  };

  const renderRoleRequestStatus = () => {
    if (roleRequestStatus === "pending")
      return "🕒 Заявка на роль библиотекаря ожидает рассмотрения";
    if (roleRequestStatus === "approved")
      return "✅ Заявка на роль библиотекаря одобрена";
    if (roleRequestStatus === "rejected")
      return "❌ Заявка на роль библиотекаря отклонена";
    return null;
  };

  if (loading) return <div className="loading">Загрузка...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="user-dashboard">
      <h2>Личный кабинет</h2>
      <div className="user-info">
        <p><strong>Имя:</strong> {user.first_name}</p>
        <p><strong>Фамилия:</strong> {user.last_name}</p>
        <p><strong>Телефон:</strong> {user.phone}</p>
        <p><strong>Статус заявки на роль библиотекаря:</strong> {renderRoleRequestStatus() || "Заявка не подана"}</p>
        {user.role !== "librarian" &&
          (roleRequestStatus === null || roleRequestStatus === "rejected") && (
            <button className="role-request-button" onClick={handleRoleRequest}>
              Подать заявку на роль библиотекаря
            </button>
        )}
      </div>

      <div className="requests-section">
        <h3>Мои заявки</h3>
        <button
          className="toggle-button"
          onClick={() => setShowRequests(!showRequests)}
        >
          {showRequests ? "Скрыть заявки" : "Показать заявки"}
        </button>

        {showRequests &&
          (requests.length === 0 ? (
            <p>У вас пока нет заявок.</p>
          ) : (
            <ul className="request-list">
              {requests.map((req) => {
                const statusClass = req.date_to_return
                  ? "returned"
                  : req.res_answer_to_user === true
                  ? "approved"
                  : req.res_answer_to_user === false
                  ? "rejected"
                  : "pending";

                return (
                  <li key={req.reservation_id} className={`request-item ${statusClass}`}>
                    <p><strong>Книга:</strong> {getBookTitleByCopyId(req.book_copy_id)}</p>
                    <p><strong>Дата заявки:</strong> {new Date(req.date_request_from_user).toLocaleString()}</p>
                    <p><strong>Статус:</strong> {renderStatus(req.res_answer_to_user, req.date_of_start_of_issuance, req.date_to_return)}</p>
                    {req.date_of_start_of_issuance && (
                      <p><strong>Дата выдачи:</strong> {new Date(req.date_of_start_of_issuance).toLocaleString()}</p>
                    )}
                    {req.date_to_return && (
                      <p><strong>Дата возврата:</strong> {new Date(req.date_to_return).toLocaleString()}</p>
                    )}
                  </li>
                );
              })}
            </ul>
          ))}
      </div>
    </div>
  );
}

export default UserDashboard;
