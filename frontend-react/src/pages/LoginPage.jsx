import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const BASE_URL = process.env.REACT_APP_API_BASE_URL; 

const LoginPage = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });
  const navigate = useNavigate();
  const [userType, setUserType] = useState("READER"); // READER, AUTHOR, ADMIN
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);

  const ADMIN_EMAIL = 'admin@bookhub.com';
  const ADMIN_PASSWORD = 'admin123';

  // 사용자 유형별 정보
  const userTypeInfo = {
    READER: {
      label: '독자',
      description: '도서를 구매하고 읽으세요',
      color: '#007bff',
      icon: '📖'
    },
    AUTHOR: {
      label: '작가',
      description: '작품을 등록하고 수익을 얻으세요',
      color: '#28a745',
      icon: '✍️'
    },
    ADMIN: {
      label: '관리자',
      description: '플랫폼을 관리하세요',
      color: '#dc3545',
      icon: '⚙️'
    }
  };

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

  const handleUserTypeChange = (type) => {
    setUserType(type);
    // 사용자 타입 변경 시 에러 초기화
    setErrors({});
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const newErrors = {};

    // 유효성 검사
    if (!formData.email) {
      newErrors.email = '이메일을 입력해주세요';
    } else if (!formData.email.includes('@')) {
      newErrors.email = '올바른 이메일 형식을 입력해주세요';
    }

    if (!formData.password) {
      newErrors.password = '비밀번호를 입력해주세요';
    } else if (formData.password.length < 6) {
      newErrors.password = '비밀번호는 6자 이상이어야 합니다';
    }

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    setIsLoading(true);

    try {
      if (userType === 'ADMIN') {
        if (
          formData.email === ADMIN_EMAIL &&
          formData.password === ADMIN_PASSWORD
        ) {
          alert('관리자 로그인 성공!');
          
          // 관리자 정보 저장
          sessionStorage.setItem('accessToken', 'admin-token');
          const adminInfo = {
            userType: 'ADMIN',
            name: '관리자',
            email: ADMIN_EMAIL,
            id: 0
          };
          sessionStorage.setItem('userInfo', JSON.stringify(adminInfo));
          
          navigate('/admin'); // 관리자 페이지로 이동
          return;
        } else {
          setErrors({ general: '관리자 계정 정보가 올바르지 않습니다.' });
          setIsLoading(false);
          return;
        }
      }

      // API 호출용 데이터 준비
      const loginData = {
        email: formData.email,
        password: formData.password,
        userType: userType // READER, AUTHOR, ADMIN
      };

      const response = await fetch(`${BASE_URL}/api/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: formData.email,
          password: formData.password,
          userType: userType,
        }),
      });
      
      const data = await response.json();
      console.log('로그인 시도:', loginData);

      if (response.ok) {
        console.log('로그인 성공:', data);
        alert(`${userTypeInfo[userType].label} 로그인 성공!`);
        
        // 토큰 저장
        sessionStorage.setItem('accessToken', data.accessToken);
        
        // 사용자 정보 저장
        const userInfo = {
          userType: userType,
          name: data.name || data.username || formData.email.split('@')[0], // 서버에서 받은 이름 또는 이메일에서 추출
          email: data.email || formData.email,
          id: data.id || data.userId
        };
        sessionStorage.setItem('userInfo', JSON.stringify(userInfo));
        
        // 사용자 타입별 리다이렉트
        switch(userType) {
          case 'READER':
            navigate('/readerMypage');
            break;
          case 'AUTHOR':
            navigate('/authorMypage');
            break;
          case 'ADMIN':
            navigate('/admin');
            break;
          default:
            navigate('/');
        }
      } else {
        console.error('로그인 실패:', data);
        setErrors({ general: data.message || '로그인에 실패했습니다. 다시 시도해주세요.' });
      }
        
    } catch (error) {
      console.error('로그인 오류:', error);
      setErrors({ general: '서버와 통신 중 오류가 발생했습니다.' });
    } finally {
      setIsLoading(false);
    }
  };

  const currentUserType = userTypeInfo[userType];

  return (
    <div style={{
      minHeight: '100vh',
      backgroundColor: '#f8f9fa',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      fontFamily: 'Arial, sans-serif',
      padding: '1rem'
    }}>
      <div style={{
        backgroundColor: '#fff',
        borderRadius: '12px',
        boxShadow: '0 8px 32px rgba(0,0,0,0.1)',
        overflow: 'hidden',
        width: '100%',
        maxWidth: '450px'
      }}>
        {/* 헤더 - 사용자 타입 선택 */}
        <div style={{
          backgroundColor: currentUserType.color,
          color: '#fff',
          padding: '1.5rem',
          textAlign: 'center'
        }}>
          <div style={{
            fontSize: '2rem',
            marginBottom: '0.5rem'
          }}>
            {currentUserType.icon}
          </div>
          <h1 style={{
            fontSize: '1.8rem',
            fontWeight: 'bold',
            margin: 0,
            marginBottom: '0.5rem'
          }}>
            BookHub
          </h1>
          <p style={{
            margin: 0,
            opacity: 0.9,
            fontSize: '0.9rem'
          }}>
            {currentUserType.description}
          </p>
        </div>

        {/* 사용자 타입 선택 탭 */}
        <div style={{
          display: 'flex',
          backgroundColor: '#f8f9fa',
          borderBottom: '1px solid #eee'
        }}>
          {Object.entries(userTypeInfo).map(([type, info]) => (
            <button
              key={type}
              onClick={() => handleUserTypeChange(type)}
              style={{
                flex: 1,
                padding: '1rem 0.5rem',
                border: 'none',
                backgroundColor: userType === type ? '#fff' : 'transparent',
                color: userType === type ? info.color : '#666',
                fontSize: '0.9rem',
                fontWeight: userType === type ? '600' : '400',
                cursor: 'pointer',
                transition: 'all 0.2s ease',
                borderBottom: userType === type ? `3px solid ${info.color}` : '3px solid transparent'
              }}
            >
              {info.icon} {info.label}
            </button>
          ))}
        </div>

        {/* 로그인 폼 */}
        <div style={{ padding: '2rem' }}>
          {errors.general && (
            <div style={{
              backgroundColor: '#f8d7da',
              color: '#721c24',
              padding: '0.8rem',
              borderRadius: '4px',
              marginBottom: '1rem',
              fontSize: '0.9rem',
              border: '1px solid #f5c6cb'
            }}>
              {errors.general}
            </div>
          )}

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
                disabled={isLoading}
                style={{
                  width: '100%',
                  padding: '0.8rem',
                  border: errors.email ? '2px solid #e74c3c' : '1px solid #ddd',
                  borderRadius: '6px',
                  fontSize: '1rem',
                  outline: 'none',
                  boxSizing: 'border-box',
                  backgroundColor: isLoading ? '#f8f9fa' : '#fff',
                  transition: 'all 0.2s ease'
                }}
                onFocus={(e) => !errors.email && (e.target.style.borderColor = currentUserType.color)}
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
                disabled={isLoading}
                style={{
                  width: '100%',
                  padding: '0.8rem',
                  border: errors.password ? '2px solid #e74c3c' : '1px solid #ddd',
                  borderRadius: '6px',
                  fontSize: '1rem',
                  outline: 'none',
                  boxSizing: 'border-box',
                  backgroundColor: isLoading ? '#f8f9fa' : '#fff',
                  transition: 'all 0.2s ease'
                }}
                onFocus={(e) => !errors.password && (e.target.style.borderColor = currentUserType.color)}
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
              disabled={isLoading}
              style={{
                width: '100%',
                padding: '0.8rem',
                backgroundColor: isLoading ? '#6c757d' : currentUserType.color,
                color: '#fff',
                border: 'none',
                borderRadius: '6px',
                fontSize: '1rem',
                fontWeight: '500',
                cursor: isLoading ? 'not-allowed' : 'pointer',
                transition: 'all 0.2s ease',
                opacity: isLoading ? 0.7 : 1
              }}
            >
              {isLoading ? (
                <span>
                  ⏳ 로그인 중...
                </span>
              ) : (
                <span>
                  {currentUserType.icon} {currentUserType.label}로 로그인
                </span>
              )}
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
              marginBottom: '1rem',
              fontSize: '0.9rem'
            }}>
              아직 계정이 없으신가요?
            </p>
            <button 
              type="button"
              disabled={isLoading}
              style={{
                width: '100%',
                padding: '0.8rem',
                backgroundColor: 'transparent',
                color: currentUserType.color,
                border: `1px solid ${currentUserType.color}`,
                borderRadius: '6px',
                fontSize: '1rem',
                fontWeight: '500',
                cursor: isLoading ? 'not-allowed' : 'pointer',
                transition: 'all 0.2s ease',
                opacity: isLoading ? 0.5 : 1
              }}
              onMouseOver={(e) => {
                if (!isLoading) {
                  e.target.style.backgroundColor = currentUserType.color;
                  e.target.style.color = '#fff';
                }
              }}
              onMouseOut={(e) => {
                if (!isLoading) {
                  e.target.style.backgroundColor = 'transparent';
                  e.target.style.color = currentUserType.color;
                }
              }}
              onClick={() => {
                if (!isLoading) {
                  console.log(`${currentUserType.label} 회원가입 페이지로 이동`);
                  navigate('/register', { state: { userType } });
                }
              }}
            >
              {currentUserType.label} 회원가입
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;