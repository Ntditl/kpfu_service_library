import React, { useState, useEffect } from 'react';
import { api } from '../../../api';

function ProfileSection() {
  const [profile, setProfile] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [form, setForm] = useState({});

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const res = await api.get('/user/profile', {
          headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
        });
        setProfile(res.data);
        setForm(res.data);
      } catch {
        console.error('Не удалось загрузить профиль');
      }
    };
    fetchProfile();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSave = async () => {
    try {
      await api.put('/user/profile', form, {
        headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
      });
      setProfile(form);
      setEditMode(false);
    } catch {
      alert('Ошибка при сохранении');
    }
  };

  if (!profile) return <p>Загрузка профиля...</p>;

  return (
    <div className="cabinet-section">
      <h2>Профиль</h2>
      {editMode ? (
        <>
          <input name="first_name" value={form.first_name} onChange={handleChange} />
          <input name="last_name" value={form.last_name} onChange={handleChange} />
          <input name="email" value={form.email} onChange={handleChange} />
          <input name="phone" value={form.phone} onChange={handleChange} />
          <button onClick={handleSave}>Сохранить</button>
        </>
      ) : (
        <>
          <p>Имя: {profile.first_name}</p>
          <p>Фамилия: {profile.last_name}</p>
          <p>Email: {profile.email}</p>
          <p>Телефон: {profile.phone}</p>
          <button onClick={() => setEditMode(true)}>Редактировать</button>
        </>
      )}
    </div>
  );
}

export default ProfileSection;
