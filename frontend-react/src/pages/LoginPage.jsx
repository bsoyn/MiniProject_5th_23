import React, { useState } from 'react';

const LoginPage = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });

  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    // 에러 메시지 제거
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const newErrors = {};

    if (!formData.email) {
      newErrors.email = '이메일을 입력해주세요';
    } else if (!formData.email.includes('@')) {
      newErrors.email = '올바른 이메일 형식을 입력해주세요';
    }

    if (!formData.password) {
      newErrors.password = '비밀번호를 입력해주세요';
    }

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    // 로그인 로직
    console.log('로그인 시도:', formData);
  };

  return (
    <div style={{
      minHeight: '100vh',
      backgroundColor: '#f8f9fa',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      fontFamily: 'Arial, sans-serif'
    }}>
      <div style={{
        backgroundColor: '#fff',
        borderRadius: '8px',
        boxShadow: '0 4px 16px rgba(0,0,0,0.1)',
        padding: '3rem',
        width: '100%',
        maxWidth: '400px'
      }}>
        {/* 로고 */}
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <h1 style={{
            fontSize: '2rem',
            fontWeight: 'bold',
            color: '#333',
            margin: 0,
            marginBottom: '0.5rem'
          }}>
            BookHub
          </h1>
          <p style={{
            color: '#666',
            margin: 0
          }}>
            로그인하여 계속하세요
          </p>
        </div>

        {/* 로그인 폼 */}
        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '1.5rem' }}>
            <label style={{
              display: 'block',
              marginBottom: '0.5rem',
              color: '#333',
              fontSize: '0.9rem',
              fontWeight: '500'
            }}>
              이메일
            </label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="your@email.com"
              style={{
                width: '100%',
                padding: '0.8rem',
                border: errors.email ? '2px solid #e74c3c' : '1px solid #ddd',
                borderRadius: '4px',
                fontSize: '1rem',
                outline: 'none',
                boxSizing: 'border-box',
                backgroundColor: '#fff'
              }}
              onFocus={(e) => e.target.style.borderColor = '#007bff'}
              onBlur={(e) => e.target.style.borderColor = errors.email ? '#e74c3c' : '#ddd'}
            />
            {errors.email && (
              <span style={{
                color: '#e74c3c',
                fontSize: '0.8rem',
                marginTop: '0.3rem',
                display: 'block'
              }}>
                {errors.email}
              </span>
            )}
          </div>

          <div style={{ marginBottom: '2rem' }}>
            <label style={{
              display: 'block',
              marginBottom: '0.5rem',
              color: '#333',
              fontSize: '0.9rem',
              fontWeight: '500'
            }}>
              비밀번호
            </label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="비밀번호를 입력하세요"
              style={{
                width: '100%',
                padding: '0.8rem',
                border: errors.password ? '2px solid #e74c3c' : '1px solid #ddd',
                borderRadius: '4px',
                fontSize: '1rem',
                outline: 'none',
                boxSizing: 'border-box',
                backgroundColor: '#fff'
              }}
              onFocus={(e) => e.target.style.borderColor = '#007bff'}
              onBlur={(e) => e.target.style.borderColor = errors.password ? '#e74c3c' : '#ddd'}
            />
            {errors.password && (
              <span style={{
                color: '#e74c3c',
                fontSize: '0.8rem',
                marginTop: '0.3rem',
                display: 'block'
              }}>
                {errors.password}
              </span>
            )}
          </div>

          <button
            type="submit"
            style={{
              width: '100%',
              padding: '0.8rem',
              backgroundColor: '#333',
              color: '#fff',
              border: 'none',
              borderRadius: '4px',
              fontSize: '1rem',
              fontWeight: '500',
              cursor: 'pointer',
              transition: 'background-color 0.2s ease'
            }}
            onMouseOver={(e) => e.target.style.backgroundColor = '#555'}
            onMouseOut={(e) => e.target.style.backgroundColor = '#333'}
          >
            로그인
          </button>
        </form>

        {/* 추가 옵션 */}
        <div style={{
          marginTop: '1.5rem',
          textAlign: 'center'
        }}>
          <a href="#" style={{
            color: '#666',
            textDecoration: 'none',
            fontSize: '0.9rem'
          }}>
            비밀번호를 잊으셨나요?
          </a>
        </div>

        {/* 구분선 */}
        <div style={{
          margin: '2rem 0',
          position: 'relative',
          textAlign: 'center'
        }}>
          <hr style={{
            border: 'none',
            borderTop: '1px solid #eee',
            margin: 0
          }} />
          <span style={{
            position: 'absolute',
            top: '-10px',
            left: '50%',
            transform: 'translateX(-50%)',
            backgroundColor: '#fff',
            padding: '0 1rem',
            color: '#666',
            fontSize: '0.9rem'
          }}>
            또는
          </span>
        </div>

        {/* 회원가입 링크 */}
        <div style={{ textAlign: 'center' }}>
          <p style={{
            color: '#666',
            margin: 0,
            marginBottom: '1rem'
          }}>
            아직 계정이 없으신가요?
          </p>
          <button style={{
            width: '100%',
            padding: '0.8rem',
            backgroundColor: 'transparent',
            color: '#333',
            border: '1px solid #ddd',
            borderRadius: '4px',
            fontSize: '1rem',
            fontWeight: '500',
            cursor: 'pointer',
            transition: 'all 0.2s ease'
          }}
          onMouseOver={(e) => {
            e.target.style.backgroundColor = '#f8f9fa';
            e.target.style.borderColor = '#333';
          }}
          onMouseOut={(e) => {
            e.target.style.backgroundColor = 'transparent';
            e.target.style.borderColor = '#ddd';
          }}
          >
            회원가입
          </button>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;