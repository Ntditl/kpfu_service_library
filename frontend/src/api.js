import axios from 'axios';

export const api = axios.create({
  baseURL: 'https://285f-178-208-64-74.ngrok-free.app',
  headers: {
    'ngrok-skip-browser-warning': 'true',
    'Content-Type': 'application/json'
  },
  timeout: 5000
});

export const checkBackendAvailable = async () => {
  try {
    await api.get('/health');
    return true;
  } catch {
    return false;
  }
};