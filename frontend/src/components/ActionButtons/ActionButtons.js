import React from "react";
import { api } from "../../api";
import "./ActionButtons.css";

function ActionButtons({ bookId, copies, refreshCopies }) {
  const userId = localStorage.getItem("user_id");
  const availableCopies = copies.filter(c => c.is_in_here && !c.is_in_reservation).length;

  const handleReserve = async () => {
    if (!userId) {
      alert("Вы должны быть авторизованы");
      return;
    }

    if (availableCopies === 0) {
      alert("Нет доступных экземпляров для бронирования.");
      return;
    }

    try {
      
      const response = await api.post("/borrowings/by-book", {
        user_id: userId,
        book_id: bookId,
        date_request_from_user: "2024-03-20T10:00:00"
      });


      if (response.status === 201 || response.status === 200) {
        alert("Заявка отправлена!");
        if (refreshCopies) refreshCopies();
      } else {
        alert("Не удалось отправить заявку");
      }
    } catch (error) {
      console.error("Ошибка при бронировании:", error);
      alert("Ошибка при бронировании");
    }
  };

  return (
    <div className="action-buttons">
      <button
        className="btn-primary"
        onClick={handleReserve}
        disabled={availableCopies === 0}
      >
        {availableCopies === 0 ? "Нет доступных экземпляров" : "Забронировать"}
      </button>
    </div>
  );
}

export default ActionButtons;


