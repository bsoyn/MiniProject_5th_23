import React, { useState } from 'react';

const RegisterPage = () => {
  const [userType, setUserType] = useState('reader'); // 'reader' or 'author'
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
    // 작가 전용 필드
    introduction: '',
    representative_work: '',
    portfolio: null
  });

  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    
    if (name === 'portfolio') {
      setFormData(prev => ({
        ...prev,
        [name]: files[0]
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }
    
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
    setFormData({
      name: '',
      email: '',
      password: '',
      confirmPassword: '',
      introduction: '',
      representative_work: '',
      portfolio: null
    });
    setErrors({});
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.name.trim()) {
      newErrors.name = '이름을 입력해주세요';
    }

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

    if (!formData.confirmPassword) {
      newErrors.confirmPassword = '비밀번호 확인을 입력해주세요';
    } else if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = '비밀번호가 일치하지 않습니다';
    }

    if (userType === 'author') {
      if (!formData.introduction.trim()) {
        newErrors.introduction = '자기소개를 입력해주세요';
      }

      if (!formData.representative_work.trim()) {
        newErrors.representative_work = '대표작을 입력해주세요';
      }

      if (!formData.portfolio) {
        newErrors.portfolio = '포트폴리오 파일을 업로드해주세요';
      }
    }

    return newErrors;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const newErrors = validateForm();

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    // 회원가입 로직
    console.log('회원가입 시도:', { userType, ...formData });
  };

  const inputStyle = (fieldName) => ({
    width: '100%',
    padding: '0.8rem',
    border: errors[fieldName] ? '2px solid #e74c3c' : '1px solid #ddd',
    borderRadius: '4px',
    fontSize: '1rem',
    outline: 'none',
    boxSizing: 'border-box',
    backgroundColor: '#fff'
  });

  return (
    <div style={{
      minHeight: '100vh',
      backgroundColor: '#f8f9fa',
      padding: '2rem 0',
      fontFamily: 'Arial, sans-serif'
    }}>
      <div style={{
        maxWidth: '500px',
        margin: '0 auto',
        backgroundColor: '#fff',
        borderRadius: '8px',
        boxShadow: '0 4px 16px rgba(0,0,0,0.1)',
        padding: '3rem'
      }}>
        {/* 헤더 */}
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
            새로운 계정을 만들어보세요
          </p>
        </div>

        {/* 사용자 타입 선택 */}
        <div style={{ marginBottom: '2rem' }}>
          <h3 style={{
            fontSize: '1.1rem',
            color: '#333',
            marginBottom: '1rem',
            fontWeight: '500'
          }}>
            회원 유형을 선택하세요
          </h3>
          <div style={{ display: 'flex', gap: '1rem' }}>
            <button
              type="button"
              onClick={() => handleUserTypeChange('reader')}
              style={{
                flex: 1,
                padding: '1rem',
                border: userType === 'reader' ? '2px solid #333' : '1px solid #ddd',
                borderRadius: '4px',
                backgroundColor: userType === 'reader' ? '#f8f9fa' : '#fff',
                cursor: 'pointer',
                transition: 'all 0.2s ease'
              }}
            >
              <div style={{ textAlign: 'center' }}>
                <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>📖</div>
                <div style={{ fontWeight: '500', color: '#333' }}>독자</div>
                <div style={{ fontSize: '0.8rem', color: '#666', marginTop: '0.3rem' }}>
                  가입시 1000P 지급
                </div>
              </div>
            </button>
            <button
              type="button"
              onClick={() => handleUserTypeChange('author')}
              style={{
                flex: 1,
                padding: '1rem',
                border: userType === 'author' ? '2px solid #333' : '1px solid #ddd',
                borderRadius: '4px',
                backgroundColor: userType === 'author' ? '#f8f9fa' : '#fff',
                cursor: 'pointer',
                transition: 'all 0.2s ease'
              }}
            >
              <div style={{ textAlign: 'center' }}>
                <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>✍️</div>
                <div style={{ fontWeight: '500', color: '#333' }}>작가</div>
                <div style={{ fontSize: '0.8rem', color: '#666', marginTop: '0.3rem' }}>
                  승인 후 작품 등록
                </div>
              </div>
            </button>
          </div>
        </div>

        {/* 회원가입 폼 */}
        <form onSubmit={handleSubmit}>
          {/* 기본 정보 */}
          <div style={{ marginBottom: '1.5rem' }}>
            <label style={{
              display: 'block',
              marginBottom: '0.5rem',
              color: '#333',
              fontSize: '0.9rem',
              fontWeight: '500'
            }}>
              이름
            </label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              placeholder="이름을 입력하세요"
              style={inputStyle('name')}
            />
            {errors.name && (
              <span style={{ color: '#e74c3c', fontSize: '0.8rem', marginTop: '0.3rem', display: 'block' }}>
                {errors.name}
              </span>
            )}
          </div>

          <div style={{ marginBottom: '1.5rem' }}>
            <label style={{
              display: 'block',
              marginBottom: '0.5rem',
              color: '#333',
              fontSize: '0.9rem',
              fontWeight: '500'
            }}>
              이메일 (아이디)
            </label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="your@email.com"
              style={inputStyle('email')}
            />
            {errors.email && (
              <span style={{ color: '#e74c3c', fontSize: '0.8rem', marginTop: '0.3rem', display: 'block' }}>
                {errors.email}
              </span>
            )}
          </div>

          <div style={{ marginBottom: '1.5rem' }}>
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
              placeholder="6자 이상 입력하세요"
              style={inputStyle('password')}
            />
            {errors.password && (
              <span style={{ color: '#e74c3c', fontSize: '0.8rem', marginTop: '0.3rem', display: 'block' }}>
                {errors.password}
              </span>
            )}
          </div>

          <div style={{ marginBottom: '1.5rem' }}>
            <label style={{
              display: 'block',
              marginBottom: '0.5rem',
              color: '#333',
              fontSize: '0.9rem',
              fontWeight: '500'
            }}>
              비밀번호 확인
            </label>
            <input
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              placeholder="비밀번호를 다시 입력하세요"
              style={inputStyle('confirmPassword')}
            />
            {errors.confirmPassword && (
              <span style={{ color: '#e74c3c', fontSize: '0.8rem', marginTop: '0.3rem', display: 'block' }}>
                {errors.confirmPassword}
              </span>
            )}
          </div>

          {/* 작가 전용 필드 */}
          {userType === 'author' && (
            <>
              <div style={{ marginBottom: '1.5rem' }}>
                <label style={{
                  display: 'block',
                  marginBottom: '0.5rem',
                  color: '#333',
                  fontSize: '0.9rem',
                  fontWeight: '500'
                }}>
                  자기소개
                </label>
                <textarea
                  name="introduction"
                  value={formData.introduction}
                  onChange={handleChange}
                  placeholder="자신을 소개해주세요"
                  rows={4}
                  style={{
                    ...inputStyle('introduction'),
                    resize: 'vertical'
                  }}
                />
                {errors.introduction && (
                  <span style={{ color: '#e74c3c', fontSize: '0.8rem', marginTop: '0.3rem', display: 'block' }}>
                    {errors.introduction}
                  </span>
                )}
              </div>

              <div style={{ marginBottom: '1.5rem' }}>
                <label style={{
                  display: 'block',
                  marginBottom: '0.5rem',
                  color: '#333',
                  fontSize: '0.9rem',
                  fontWeight: '500'
                }}>
                  대표작
                </label>
                <input
                  type="text"
                  name="representative_work"
                  value={formData.representative_work}
                  onChange={handleChange}
                  placeholder="대표작 제목을 입력하세요"
                  style={inputStyle('representative_work')}
                />
                {errors.representative_work && (
                  <span style={{ color: '#e74c3c', fontSize: '0.8rem', marginTop: '0.3rem', display: 'block' }}>
                    {errors.representative_work}
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
                  포트폴리오
                </label>
                <div style={{
                  border: errors.portfolio ? '2px solid #e74c3c' : '2px dashed #ddd',
                  borderRadius: '4px',
                  padding: '2rem',
                  textAlign: 'center',
                  backgroundColor: '#fafafa',
                  cursor: 'pointer',
                  transition: 'all 0.2s ease'
                }}
                onMouseOver={(e) => {
                  if (!errors.portfolio) {
                    e.target.style.borderColor = '#007bff';
                    e.target.style.backgroundColor = '#f0f8ff';
                  }
                }}
                onMouseOut={(e) => {
                  if (!errors.portfolio) {
                    e.target.style.borderColor = '#ddd';
                    e.target.style.backgroundColor = '#fafafa';
                  }
                }}
                >
                  <input
                    type="file"
                    name="portfolio"
                    onChange={handleChange}
                    accept=".pdf,.doc,.docx,.hwp"
                    style={{ display: 'none' }}
                    id="portfolio-upload"
                  />
                  <label htmlFor="portfolio-upload" style={{ cursor: 'pointer' }}>
                    <div style={{ fontSize: '2rem', marginBottom: '0.5rem' }}>📄</div>
                    <div style={{ color: '#333', fontWeight: '500', marginBottom: '0.3rem' }}>
                      {formData.portfolio ? formData.portfolio.name : '포트폴리오 파일 업로드'}
                    </div>
                    <div style={{ color: '#666', fontSize: '0.8rem' }}>
                      PDF, DOC, DOCX, HWP 파일 지원
                    </div>
                  </label>
                </div>
                {errors.portfolio && (
                  <span style={{ color: '#e74c3c', fontSize: '0.8rem', marginTop: '0.3rem', display: 'block' }}>
                    {errors.portfolio}
                  </span>
                )}
              </div>
            </>
          )}

          {/* 회원가입 버튼 */}
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
              transition: 'background-color 0.2s ease',
              marginBottom: '1.5rem'
            }}
            onMouseOver={(e) => e.target.style.backgroundColor = '#555'}
            onMouseOut={(e) => e.target.style.backgroundColor = '#333'}
          >
            {userType === 'author' ? '작가 등록 신청' : '회원가입'}
          </button>

          {/* 작가 등록 안내 */}
          {userType === 'author' && (
            <div style={{
              backgroundColor: '#f8f9fa',
              padding: '1rem',
              borderRadius: '4px',
              marginBottom: '1.5rem',
              border: '1px solid #e9ecef'
            }}>
              <h4 style={{
                color: '#333',
                fontSize: '0.9rem',
                fontWeight: '500',
                marginBottom: '0.5rem'
              }}>
                📋 작가 등록 안내
              </h4>
              <ul style={{
                color: '#666',
                fontSize: '0.8rem',
                margin: 0,
                paddingLeft: '1.2rem'
              }}>
                <li>관리자 승인 후 로그인이 가능합니다</li>
                <li>승인까지 1-3일 정도 소요됩니다</li>
                <li>승인 결과는 이메일로 알려드립니다</li>
              </ul>
            </div>
          )}

          {/* 독자 혜택 안내 */}
          {userType === 'reader' && (
            <div style={{
              backgroundColor: '#f0f8ff',
              padding: '1rem',
              borderRadius: '4px',
              marginBottom: '1.5rem',
              border: '1px solid #cce7ff'
            }}>
              <h4 style={{
                color: '#333',
                fontSize: '0.9rem',
                fontWeight: '500',
                marginBottom: '0.5rem'
              }}>
                🎉 신규 회원 혜택
              </h4>
              <p style={{
                color: '#666',
                fontSize: '0.8rem',
                margin: 0
              }}>
                회원가입 완료시 1,000 포인트를 즉시 지급해드립니다!
              </p>
            </div>
          )}
        </form>

        {/* 로그인 링크 */}
        <div style={{ textAlign: 'center' }}>
          <p style={{
            color: '#666',
            margin: 0,
            fontSize: '0.9rem'
          }}>
            이미 계정이 있으신가요?{' '}
            <a href="#" style={{
              color: '#333',
              textDecoration: 'none',
              fontWeight: '500'
            }}>
              로그인하기
            </a>
          </p>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;