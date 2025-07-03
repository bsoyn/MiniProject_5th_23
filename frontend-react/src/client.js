// src/api/client.js

// API Gateway 주소
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

// 기본 fetch 래퍼 클래스
class ApiClient {
  constructor(baseURL, timeout = 10000) {
    this.baseURL = baseURL;
    this.timeout = timeout;
    this.defaultHeaders = {
      'Content-Type': 'application/json',
    };
  }

  // 요청 전 처리 (인증 토큰 추가)
  getHeaders(customHeaders = {}) {
    const token = localStorage.getItem('authToken');
    const headers = { ...this.defaultHeaders, ...customHeaders };
    
    if (token) {
      headers.Authorization = `Bearer ${token}`;
    }
    
    return headers;
  }

  // 타임아웃 처리를 위한 AbortController 생성
  createAbortController() {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), this.timeout);
    
    return { controller, timeoutId };
  }

  // 응답 처리 및 에러 핸들링
  async handleResponse(response) {
    // 401 에러 처리
    if (response.status === 401) {
      localStorage.removeItem('authToken');
      window.location.href = '/login';
      throw new Error('Unauthorized');
    }

    // 응답이 성공적이지 않은 경우
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      const error = new Error(`HTTP ${response.status}: ${response.statusText}`);
      error.response = {
        status: response.status,
        statusText: response.statusText,
        data: errorData
      };
      throw error;
    }

    return response;
  }

  // 공통 요청 메서드
  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;
    const { controller, timeoutId } = this.createAbortController();

    try {
      const response = await fetch(url, {
        ...options,
        headers: this.getHeaders(options.headers),
        signal: controller.signal,
      });

      clearTimeout(timeoutId);
      return await this.handleResponse(response);
    } catch (error) {
      clearTimeout(timeoutId);
      
      if (error.name === 'AbortError') {
        throw new Error('Request timeout');
      }
      
      throw error;
    }
  }

  // GET 요청
  async get(endpoint, config = {}) {
    const response = await this.request(endpoint, {
      method: 'GET',
      ...config,
    });
    return response.json();
  }

  // POST 요청
  async post(endpoint, data, config = {}) {
    const response = await this.request(endpoint, {
      method: 'POST',
      body: JSON.stringify(data),
      ...config,
    });
    return response.json();
  }

  // PUT 요청
  async put(endpoint, data, config = {}) {
    const response = await this.request(endpoint, {
      method: 'PUT',
      body: JSON.stringify(data),
      ...config,
    });
    return response.json();
  }

  // PATCH 요청
  async patch(endpoint, data, config = {}) {
    const response = await this.request(endpoint, {
      method: 'PATCH',
      body: JSON.stringify(data),
      ...config,
    });
    return response.json();
  }

  // DELETE 요청
  async delete(endpoint, config = {}) {
    const response = await this.request(endpoint, {
      method: 'DELETE',
      ...config,
    });
    return response.json();
  }
}

// 인스턴스 생성 및 내보내기
const apiClient = new ApiClient(API_BASE_URL);

export default apiClient;