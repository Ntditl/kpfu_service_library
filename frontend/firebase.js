import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';

const firebaseConfig = {
  apiKey: "AIzaSyCI-kSNvt1PBpoCIb6MlEBJr8RL1e05-PM",
  authDomain: "librarykpfu.firebaseapp.com",
  projectId: "librarykpfu",
  storageBucket: "librarykpfu.appspot.com",
  messagingSenderId: "115371258769669269385",
  appId: "1:115371258769669269385:web:your-app-id"
};

const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export default app; 