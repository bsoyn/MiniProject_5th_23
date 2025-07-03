import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const BASE_URL = process.env.REACT_APP_API_BASE_URL;

const AuthorMyPage = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // 작가 정보
  const [authorInfo, setAuthorInfo] = useState({
    name: '',
    email: '',
    introduction: '',
    representativeWork: '',
    joinDate: '',
    totalSales: 0,
    totalBooks: 0
  });

  // 작가의 도서 목록
  const [authorBooks, setAuthorBooks] = useState([]);

  // 페이지 로드 시 작가 정보 가져오기
  useEffect(() => {
    fetchAuthorInfo();
    fetchAuthorBooks();
  }, []);

  // JWT 토큰에서 사용자 정보 추출 (개선된 버전)
  const getUserInfoFromToken = (token) => {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      console.log('JWT Payload:', payload);
      return {
        userId: payload.sub || payload.userId,
        userName: payload.name,
        userType: payload.type?.[0] || payload.userType
      };
    } catch (error) {
      console.error('토큰 파싱 실패:', error);
      return null;
    }
  };

  // 도서 등록 페이지로 이동 (사용자 정보 저장 포함)
  const handleBookRegister = () => {
    const token = sessionStorage.getItem('accessToken');
    if (!token) {
      alert('로그인이 필요합니다.');
      navigate('/login');
      return;
    }

    const userInfo = getUserInfoFromToken(token);
    if (!userInfo || !userInfo.userId) {
      alert('사용자 정보를 가져올 수 없습니다.');
      return;
    }

    // BookRegisterPage에서 사용할 사용자 정보를 sessionStorage에 저장
    sessionStorage.setItem('userInfo', JSON.stringify(userInfo));
    navigate('/bookRegister');
  };

  // 작가 정보 조회
  const fetchAuthorInfo = async () => {
    try {
      const token = sessionStorage.getItem('accessToken');
      if (!token) {
        navigate('/login');
        return;
      }

      const userInfo = getUserInfoFromToken(token);
      if (!userInfo || !userInfo.userId) {
        setError('사용자 정보를 가져올 수 없습니다. 다시 로그인해주세요.');
        return;
      }

      const response = await fetch(`${BASE_URL}/authors/${userInfo.userId}`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        const data = await response.json();
        setAuthorInfo({
          name: data.name || userInfo.userName || '작가',
          email: data.email || '',
          introduction: data.bio || '작가 소개를 입력해주세요.',
          representativeWork: data.majorWork || '대표작을 입력해주세요.',
          joinDate: data.createdAt || new Date().toISOString(),
          totalSales: 0,
          totalBooks: authorBooks.length
        });
      } else if (response.status === 401) {
        sessionStorage.removeItem('accessToken');
        sessionStorage.removeItem('userInfo');
        navigate('/login');
      } else if (response.status === 404) {
        setError('작가 정보를 찾을 수 없습니다. 관리자에게 문의해주세요.');
      } else {
        setError('작가 정보를 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요.');
      }
    } catch (error) {
      console.error('작가 정보 조회 실패:', error);
      setError('서버 연결에 실패했습니다. 인터넷 연결을 확인해주세요.');
    }
  };

  // 작가의 도서 목록 조회
  const fetchAuthorBooks = async () => {
    try {
      const token = sessionStorage.getItem('accessToken');
      if (!token) {
        navigate('/login');
        return;
      }

      const response = await fetch(`${BASE_URL}/readMyBooks`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (response.ok) {
        const data = await response.json();
        // Spring Data REST 응답 형식에 맞게 처리
        const books = data._embedded ? data._embedded.readMyBooks : [];
        setAuthorBooks(books || []);
        // 도서 수 업데이트
        setAuthorInfo(prev => ({
          ...prev,
          totalBooks: books.length
        }));
      } else if (response.status === 401) {
        sessionStorage.removeItem('accessToken');
        navigate('/login');
      } else {
        console.error('도서 목록 조회 실패');
      }
    } catch (error) {
      console.error('도서 목록 조회 실패:', error);
    } finally {
      setLoading(false);
    }
  };

  // 로그아웃
  const handleLogout = () => {
    sessionStorage.removeItem('accessToken');
    sessionStorage.removeItem('userInfo');
    navigate('/login');
  };

  const getStatusColor = (status) => {
    switch (status) {
      case '판매중': return '#28a745';
      case '검토중': return '#ffc107';
      case '판매중지': return '#dc3545';
      default: return '#6c757d';
    }
  };

  // 로딩 상태 처리
  if (loading) {
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
          fontSize: '1.2rem',
          color: '#666'
        }}>
          로딩 중...
        </div>
      </div>
    );
  }

  return (
    <div style={{
      minHeight: '100vh',
      backgroundColor: '#f8f9fa',
      fontFamily: 'Arial, sans-serif'
    }}>
      {/* 헤더 */}
      <header style={{
        backgroundColor: '#fff',
        boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
        padding: '1rem 0'
      }}>
        <div style={{
          maxWidth: '1200px',
          margin: '0 auto',
          padding: '0 2rem',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center'
        }}>
          <h1 
            onClick={() => navigate('/')}
            style={{
              fontSize: '1.8rem',
              fontWeight: 'bold',
              color: '#333',
              margin: 0,
              cursor: 'pointer'
            }}
          >
            BookHub
          </h1>
          <nav style={{ display: 'flex', gap: '1rem' }}>
            <button
              onClick={() => navigate('/books')}
              style={{
                padding: '0.5rem 1rem',
                backgroundColor: 'transparent',
                border: '1px solid #666',
                borderRadius: '4px',
                color: '#666',
                cursor: 'pointer',
                fontSize: '0.9rem'
              }}
            >
              도서 목록
            </button>
            <button
              onClick={() => navigate('/')}
              style={{
                padding: '0.5rem 1rem',
                backgroundColor: '#28a745',
                border: 'none',
                borderRadius: '4px',
                color: '#fff',
                cursor: 'pointer',
                fontSize: '0.9rem'
              }}
            >
              홈으로
            </button>
            <button
              onClick={handleLogout}
              style={{
                padding: '0.5rem 1rem',
                backgroundColor: '#dc3545',
                color: 'white',
                border: 'none',
                borderRadius: '4px',
                cursor: 'pointer',
                fontSize: '0.9rem'
              }}
            >
              로그아웃
            </button>
          </nav>
        </div>
      </header>

      <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '2rem' }}>
        {/* 에러 메시지 */}
        {error && (
          <div style={{
            backgroundColor: '#f8d7da',
            color: '#721c24',
            padding: '0.8rem',
            borderRadius: '4px',
            marginBottom: '1rem',
            fontSize: '0.9rem',
            border: '1px solid #f5c6cb'
          }}>
            {error}
          </div>
        )}

        {/* 페이지 제목 */}
        <div style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          marginBottom: '2rem'
        }}>
          <h2 style={{
            fontSize: '2rem',
            fontWeight: 'bold',
            color: '#333',
            margin: 0
          }}>
            작가 페이지
          </h2>
          <button
            onClick={handleBookRegister}
            style={{
              padding: '0.8rem 1.5rem',
              backgroundColor: '#28a745',
              color: '#fff',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer',
              fontSize: '1rem',
              fontWeight: '500'
            }}
          >
            + 새 도서 등록
          </button>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: '2rem' }}>
          {/* 왼쪽 사이드바 */}
          <div>
            {/* 작가 정보 카드 */}
            <div style={{
              backgroundColor: '#fff',
              borderRadius: '8px',
              padding: '2rem',
              boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              marginBottom: '1.5rem'
            }}>
              <div style={{ textAlign: 'center', marginBottom: '1.5rem' }}>
                <div style={{
                  width: '80px',
                  height: '80px',
                  borderRadius: '50%',
                  backgroundColor: '#28a745',
                  color: '#fff',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: '2rem',
                  margin: '0 auto 1rem'
                }}>
                  ✍️
                </div>
                <h3 style={{ color: '#333', marginBottom: '0.5rem' }}>{authorInfo.name}</h3>
                <p style={{ color: '#666', fontSize: '0.9rem' }}>{authorInfo.email}</p>
                <p style={{ color: '#999', fontSize: '0.8rem' }}>
                  작가 등록일: {new Date(authorInfo.joinDate).toLocaleDateString()}
                </p>
              </div>

              <div style={{
                padding: '1rem',
                backgroundColor: '#f8f9fa',
                borderRadius: '4px',
                marginBottom: '1rem'
              }}>
                <h4 style={{ color: '#333', marginBottom: '0.5rem' }}>자기소개</h4>
                <p style={{ color: '#666', fontSize: '0.9rem', lineHeight: '1.5' }}>
                  {authorInfo.introduction}
                </p>
              </div>

              <div style={{
                padding: '1rem',
                backgroundColor: '#e8f5e8',
                borderRadius: '4px'
              }}>
                <h4 style={{ color: '#333', marginBottom: '0.5rem' }}>대표작</h4>
                <p style={{ color: '#28a745', fontWeight: '500' }}>
                  {authorInfo.representativeWork}
                </p>
              </div>
            </div>

            {/* 통계 카드 */}
            <div style={{
              backgroundColor: '#fff',
              borderRadius: '8px',
              padding: '1.5rem',
              boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
            }}>
              <h4 style={{ color: '#333', marginBottom: '1rem' }}>작가 통계</h4>
              
              <div style={{ marginBottom: '1rem' }}>
                <div style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  marginBottom: '0.5rem'
                }}>
                  <span style={{ color: '#666' }}>등록 도서</span>
                  <span style={{ fontWeight: 'bold', color: '#28a745' }}>
                    {authorInfo.totalBooks}권
                  </span>
                </div>
              </div>

              <div style={{ marginBottom: '1rem' }}>
                <div style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  marginBottom: '0.5rem'
                }}>
                  <span style={{ color: '#666' }}>총 판매량</span>
                  <span style={{ fontWeight: 'bold', color: '#007bff' }}>
                    {authorBooks.length}권
                  </span>
                </div>
              </div>

              <div>
                <div style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  marginBottom: '0.5rem'
                }}>
                  <span style={{ color: '#666' }}>총 수익</span>
                  <span style={{ fontWeight: 'bold', color: '#ffc107' }}>
                    {authorBooks.reduce((sum, book) => sum + (book.price || 0), 0).toLocaleString()}P
                  </span>
                </div>
              </div>
            </div>
          </div>

          {/* 오른쪽 메인 컨텐츠 */}
          <div>
            {/* 내 도서 목록 */}
            <div style={{
              backgroundColor: '#fff',
              borderRadius: '8px',
              padding: '2rem',
              boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
            }}>
              <h3 style={{
                color: '#333',
                marginBottom: '1.5rem',
                fontSize: '1.5rem',
                fontWeight: 'bold'
              }}>
                내 도서 목록 ({authorBooks.length}권)
              </h3>

              {authorBooks.length > 0 ? (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                  {authorBooks.map(book => (
                    <div key={book.id} style={{
                      border: '1px solid #e5e5e5',
                      borderRadius: '8px',
                      padding: '1.5rem',
                      backgroundColor: '#fafafa'
                    }}>
                      <div style={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'flex-start',
                        marginBottom: '1rem'
                      }}>
                        <div style={{ flex: 1 }}>
                          <h4 style={{
                            color: '#333',
                            marginBottom: '0.5rem',
                            fontSize: '1.2rem'
                          }}>
                            {book.title}
                          </h4>
                          <p style={{ color: '#666', marginBottom: '0.5rem' }}>
                            {book.summary || '설명 없음'}
                          </p>
                          <p style={{ color: '#999', fontSize: '0.8rem' }}>
                            카테고리: {book.category || '미분류'}
                          </p>
                        </div>
                        <div style={{
                          backgroundColor: getStatusColor(book.status || '판매중'),
                          color: '#fff',
                          padding: '0.3rem 0.8rem',
                          borderRadius: '20px',
                          fontSize: '0.8rem',
                          fontWeight: '500'
                        }}>
                          {book.status || '판매중'}
                        </div>
                      </div>

                      <div style={{
                        display: 'grid',
                        gridTemplateColumns: 'repeat(4, 1fr)',
                        gap: '1rem',
                        padding: '1rem',
                        backgroundColor: '#fff',
                        borderRadius: '4px'
                      }}>
                        <div style={{ textAlign: 'center' }}>
                          <div style={{ color: '#666', fontSize: '0.8rem', marginBottom: '0.3rem' }}>
                            가격
                          </div>
                          <div style={{ fontWeight: 'bold', color: '#333' }}>
                            {book.price ? book.price.toLocaleString() : '0'}P
                          </div>
                        </div>
                        <div style={{ textAlign: 'center' }}>
                          <div style={{ color: '#666', fontSize: '0.8rem', marginBottom: '0.3rem' }}>
                            카테고리
                          </div>
                          <div style={{ fontWeight: 'bold', color: '#007bff' }}>
                            {book.category || '미분류'}
                          </div>
                        </div>
                        <div style={{ textAlign: 'center' }}>
                          <div style={{ color: '#666', fontSize: '0.8rem', marginBottom: '0.3rem' }}>
                            상태
                          </div>
                          <div style={{ fontWeight: 'bold', color: '#28a745' }}>
                            등록됨
                          </div>
                        </div>
                        <div style={{ textAlign: 'center' }}>
                          <button 
                            onClick={() => navigate(`/bookEdit/${book.id}`)}
                            style={{
                              padding: '0.4rem 0.8rem',
                              backgroundColor: '#6c757d',
                              color: '#fff',
                              border: 'none',
                              borderRadius: '4px',
                              cursor: 'pointer',
                              fontSize: '0.8rem'
                            }}
                          >
                            수정
                          </button>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <div style={{
                  textAlign: 'center',
                  padding: '3rem',
                  color: '#666'
                }}>
                  <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>📝</div>
                  <p>아직 등록한 도서가 없습니다.</p>
                  <button
                    onClick={handleBookRegister}
                    style={{
                      marginTop: '1rem',
                      padding: '0.8rem 1.5rem',
                      backgroundColor: '#28a745',
                      color: '#fff',
                      border: 'none',
                      borderRadius: '4px',
                      cursor: 'pointer'
                    }}
                  >
                    첫 번째 도서 등록하기
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
};

export default AuthorMyPage;
