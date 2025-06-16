import axios from 'axios';

export const api = axios.create({
  baseURL: 'https://9170-178-208-64-74.ngrok-free.app',
  headers: {
    'ngrok-skip-browser-warning': 'true',
    'Content-Type': 'application/json'
  },
  timeout: 15000
});



export const getUserBorrowings = (userId) =>
  api.get(`/borrowings?user_id=${userId}`);

export const getBookCopies = () =>
  api.get("/book-copies");

export const getBooks = () =>
  api.get("/books");

export const updateBorrowingStatus = (id, data) =>
  api.put(`/borrowings/${id}`, data);

export const getRoleRequests = (userId) =>
  api.get(`/borrowings-to-role${userId ? `?user_id=${userId}` : ''}`);

export const createRoleRequest = (data) =>
  api.post("/borrowings-to-role", data);

export const updateRoleRequestStatus = (id, data) =>
  api.put(`/borrowings-to-role/${id}`, data);

export const updateUserRole = (id, data) =>
  api.put(`/users/${id}`, data);