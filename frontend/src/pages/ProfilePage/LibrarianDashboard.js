import React, { useEffect, useState } from "react";
import "./LibrarianDashboard.css";
import { api } from "../../api";

function LibrarianDashboard() {
  const [requests, setRequests] = useState([]);
  const [bookCopies, setBookCopies] = useState([]);
  const [books, setBooks] = useState([]);
  const [users, setUsers] = useState([]);
  const [showRequests, setShowRequests] = useState(false);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [borrowingsRes, copiesRes, booksRes, usersRes] = await Promise.all([
        api.get("/borrowings"),
        api.get("/book-copies"),
        api.get("/books"),
        api.get("/users"),
      ]);

      setRequests(borrowingsRes.data);
      setBookCopies(copiesRes.data);
      setBooks(booksRes.data);
      setUsers(usersRes.data);
    } catch (error) {
      console.error("Ошибка при загрузке данных:", error);
    }
  };

  const getBookTitle = (copyId) => {
    const copy = bookCopies.find(c => c.copy_id === copyId);
    const book = books.find(b => b.bookId === copy?.book_id);
    return book?.title || "Неизвестно";
  };

  const getUserName = (userId) => {
    const user = users.find(u => u.user_id === userId);
    return user ? `${user.first_name} ${user.last_name}` : "Неизвестный пользователь";
  };

  const updateBookCopyReservation = async (copyId, isReserved) => {
    try {
      await api.put(`/book-copies/${copyId}/reservation`, {
        is_in_reservation: isReserved
      });
      setBookCopies(prev =>
        prev.map(copy =>
          copy.copy_id === copyId
            ? { ...copy, is_in_reservation: isReserved }
            : copy
        )
      );
    } catch (error) {
      console.error("Ошибка при обновлении резервации:", error);
    }
  };

  const updateBookCopyLocation = async (copyId, isInHere) => {
    try {
      await api.put(`/book-copies/${copyId}/location`, {
        is_in_here: isInHere
      });
      setBookCopies(prev =>
        prev.map(copy =>
          copy.copy_id === copyId
            ? { ...copy, is_in_here: isInHere }
            : copy
        )
      );
    } catch (error) {
      console.error("Ошибка при обновлении местоположения:", error);
    }
  };

  const handleDecision = async (reservationId, decision) => {
    try {
      const now = new Date().toISOString().slice(0, 16);

      await api.put(`/borrowings/${reservationId}`, {
        res_answer_to_user: decision,
        date_answer_to_request: now,
      });

      const request = requests.find(r => r.reservation_id === reservationId);
      if (decision === true) {
        await updateBookCopyReservation(request.book_copy_id, true);
      }

      setRequests(prev =>
        prev.map(r =>
          r.reservation_id === reservationId
            ? {
                ...r,
                res_answer_to_user: decision,
                date_answer_to_request: now
              }
            : r
        )
      );
    } catch (error) {
      console.error("Ошибка при принятии решения:", error);
    }
  };

  const handleIssueBook = async (reservationId) => {
    try {
      const now = new Date().toISOString().slice(0, 16);

      const request = requests.find(r => r.reservation_id === reservationId);
      await updateBookCopyLocation(request.book_copy_id, false);

      await api.put(`/borrowings/${reservationId}`, {
        date_of_start_of_issuance: now
      });

      setRequests(prev =>
        prev.map(r =>
          r.reservation_id === reservationId
            ? {
                ...r,
                date_of_start_of_issuance: now
              }
            : r
        )
      );
    } catch (error) {
      console.error("Ошибка при выдаче книги:", error);
    }
  };

  const handleReturnBook = async (reservationId) => {
    try {
      const now = new Date().toISOString().slice(0, 16);

      const request = requests.find(r => r.reservation_id === reservationId);
      await updateBookCopyReservation(request.book_copy_id, false);
      await updateBookCopyLocation(request.book_copy_id, true);

      await api.put(`/borrowings/${reservationId}/return-date`, {
        date_to_return: now
      });

      setRequests(prev =>
        prev.map(r =>
          r.reservation_id === reservationId
            ? {
                ...r,
                date_to_return: now
              }
            : r
        )
      );
    } catch (error) {
      console.error("Ошибка при возврате книги:", error);
    }
  };

  const renderStatus = (status, date_of_start_of_issuance, date_of_return) => {
    if (date_of_return) return "📚 Книга сдана";
    if (status === true && date_of_start_of_issuance) return "📖 Книга выдана";
    if (status === true) return "✅ Одобрено";
    if (status === false) return "❌ Отклонено";
    return "🕒 Ожидает";
  };

  return (
    <div className="librarian-dashboard">
      <h2>Заявки пользователей</h2>
      <button className="toggle-button" onClick={() => setShowRequests(!showRequests)}>
        {showRequests ? "Скрыть заявки" : "Показать заявки"}
      </button>

      {showRequests && (
        requests.length === 0 ? (
          <p>Нет заявок.</p>
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
                  <p><strong>Пользователь:</strong> {getUserName(req.user_id)}</p>
                  <p><strong>Книга:</strong> {getBookTitle(req.book_copy_id)}</p>
                  <p><strong>Дата запроса:</strong> {new Date(req.date_request_from_user).toLocaleString()}</p>
                  <p><strong>Статус:</strong> {renderStatus(req.res_answer_to_user, req.date_of_start_of_issuance, req.date_to_return)}</p>

                  {req.res_answer_to_user === null && (
                    <div className="decision-buttons">
                      <button onClick={() => handleDecision(req.reservation_id, true)}>Одобрить</button>
                      <button onClick={() => handleDecision(req.reservation_id, false)}>Отклонить</button>
                    </div>
                  )}

                  {req.res_answer_to_user === true && !req.date_of_start_of_issuance && (
                    <div className="issue-buttons">
                      <button
                        onClick={() => handleIssueBook(req.reservation_id)}
                        className="issue-button"
                      >
                        Выдать книгу
                      </button>
                    </div>
                  )}

                  {req.res_answer_to_user === true && req.date_of_start_of_issuance && !req.date_to_return && (
                    <div className="return-buttons">
                      <button
                        onClick={() => handleReturnBook(req.reservation_id)}
                        className="return-button"
                      >
                        Книга возвращена
                      </button>
                    </div>
                  )}

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
        )
      )}
    </div>
  );
}

export default LibrarianDashboard;
