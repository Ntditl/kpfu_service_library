import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Header from "./components/Header";
import Footer from "./components/Footer";
import BookPage from "./pages/BookPage/BookPage";
import CatalogPage from "./pages/CatalogPage/CatalogPage";
import AddBookPage from "./pages/AddBookPage/AddBookPage"; 
import LoginPage from "./pages/LoginPage/LoginPage";
import RegisterPage from "./pages/RegisterPage/RegisterPage";
import { AuthProvider } from './contexts/AuthContext';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Header />
          <main>
            <Routes>
              <Route path="/" element={<CatalogPage />} />
              <Route path="/book/:bookId" element={<BookPage />} />
              <Route path="/add-book" element={<AddBookPage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<RegisterPage />} />
            </Routes>
          </main>
          <Footer />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
