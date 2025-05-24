import React, { useEffect, useState } from 'react';
import { api } from '../../api';

function UserCabinetPage() {
  const [reservations, setReservations] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchReservations = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await api.get('/user/reservations', {
          headers: { Authorization: `Bearer ${token}` },
        });
        setReservations(response.data);
      } catch (err) {
        console.error(err);
        setError('Ошибка при загрузке заявок');
      }
    };

    fetchReservations();
  }, []);

  return (
    <div className="cabinet-page">
      <h2>Мои заявки на книги</h2>
      {error && <p className="error">{error}</p>}
      <ul className="reservation-list">
        {reservations.map(res => (
          <li key={res.reservation_id} className="reservation-item">
            <strong>{res.book_title}</strong> — статус: {res.status}
            <div>Дата заявки: {new Date(res.reservation_date).toLocaleDateString()}</div>
            <div>Действует до: {new Date(res.expiration_date).toLocaleDateString()}</div>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default UserCabinetPage;
