
// 환경변수에서 API 주소 가져오기
export const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8088';

// API 호출을 위한 공통 함수
export const apiCall = async (endpoint, options = {}) => {
  const url = `${API_BASE_URL}${endpoint}`;
  
  console.log(`API 호출: ${url}`);
  
  const defaultOptions = {
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
  };

  const mergedOptions = {
    ...defaultOptions,
    ...options,
    headers: {
      ...defaultOptions.headers,
      ...options.headers,
    },
  };

  try {
    const response = await fetch(url, mergedOptions);
    return response;
  } catch (error) {
    console.error(`API 호출 실패: ${url}`, error);
    throw error;
  }
};

// 환경 확인용 함수
export const getCurrentEnvironment = () => {
  return process.env.NODE_ENV || 'development';
};

export const isProduction = () => {
  return process.env.NODE_ENV === 'production';
};

export const isDevelopment = () => {
  return process.env.NODE_ENV === 'development';
};