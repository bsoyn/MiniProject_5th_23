import apiClient from './client.js'

export const bookService = {
  // 도서 목록 조회 - GET /api/books
  getBooks: async (page) => {
    const response = await apiClient.get(`/books/page/${page}`);
    console.log('응답:', response.books);
    return response.books;
  },

  // 도서 상세 조회 - GET /api/books/:id
  getBook: async (bookId) => {
    const response = await apiClient.get(`/api/books/${bookId}`);
    return response.data;
  },

  // 도서 생성 - POST /api/books
  createBook: async (bookData) => {
    const response = await apiClient.post('/api/books', bookData);
    return response.data;
  },

  // 도서 수정 - PUT /api/books/:id
  updateBook: async (bookId, bookData) => {
    const response = await apiClient.put(`/api/books/${bookId}`, bookData);
    return response.data;
  },

  // 도서 삭제 - DELETE /api/books/:id
  deleteBook: async (bookId) => {
    const response = await apiClient.delete(`/api/books/${bookId}`);
    return response.data;
  },

  // 도서 구매 - POST /api/books/:id/purchase
  purchaseBook: async (bookId, paymentData) => {
    const response = await apiClient.post(`/api/books/${bookId}/purchase`, paymentData);
    return response.data;
  }
};