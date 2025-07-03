import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import MainPage from './pages/MainPage'; 
import LoginPage from './pages/LoginPage'; 
import RegisterPage from './pages/RegisterPage'; 
import ReaderMyPage from './pages/ReaderMyPage'; 
import AuthorMyPage from './pages/AuthorMyPage'; 
import BookListPage from './pages/BookListPage';
import BookDetailPage from './pages/BookDetailPage';
import BookRegisterPage from './pages/BookRegisterPage'; 

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/readerMypage" element={<ReaderMyPage />} />
        <Route path="/authorMypage" element={<AuthorMyPage />} />
        <Route path="/bookListPage" element={<BookListPage />}/>
        <Route path="/books/de" element={<BookDetailPage />}/>
        <Route path="/bookRegister" element={<BookRegisterPage />} />

        {/*v페이지 추가해주기 */}
      </Routes>
    </Router>
  );
}

export default App;
