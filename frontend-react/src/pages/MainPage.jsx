import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { purchaseBook } from '../purchasebook_api/purchasebook';

const MainPage = () => {
    const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState('');
  const [user, setUser] = useState(null); // 실제 로그인 상태 관리

  // 컴포넌트 마운트 시 로그인 상태 확인
  useEffect(() => {
    checkLoginStatus();
  }, []);

  const checkLoginStatus = () => {
    const token = sessionStorage.getItem('accessToken');
    const userInfo = sessionStorage.getItem('userInfo');
    
    if (token && userInfo) {
      try {
        const parsedUserInfo = JSON.parse(userInfo);
        setUser(parsedUserInfo);
      } catch (error) {
        console.error('사용자 정보 파싱 오류:', error);
        // 잘못된 정보가 있으면 로그아웃 처리
        handleLogout();
      }
    }
  };

  const handleLogout = () => {
    sessionStorage.removeItem('accessToken');
    sessionStorage.removeItem('userInfo');
    setUser(null);
    alert('로그아웃되었습니다.');
  };

  const handleProfileClick = () => {
    if (user?.userType === 'READER') {
      navigate('/readerMypage');
    } else if (user?.userType === 'AUTHOR') {
      navigate('/authorMypage');
    } else if (user?.userType === 'ADMIN') {
      navigate('/admin');
    }
  };

  const handlePurchase = async (bookId) => {
    if (!user || user.userType !== 'READER') {
      alert('구매하려면 먼저 독자로 로그인하세요.');
      navigate('/login');
      return;
    }

    try {
      const result = await purchaseBook(user.id, bookId);
      alert(`구매 성공: ${result.status}`);
    } catch (error) {
      console.error(error);
      alert('구매에 실패했습니다.');
    }
  };

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
          
          <nav style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
            {/* 도서 목록 조회 버튼 */}
            <button
              onClick={() => navigate('/bookListPage')}
              style={{
                padding: '0.5rem 1rem',
                backgroundColor: 'transparent',
                border: '1px solid #007bff',
                borderRadius: '4px',
                color: '#007bff',
                cursor: 'pointer',
                fontSize: '0.9rem'
              }}
            >
              도서 목록
            </button>

            {/* 로그인 상태에 따른 조건부 렌더링 */}
            {user ? (
              <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                <div 
                  onClick={handleProfileClick}
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '0.5rem',
                    padding: '0.5rem 1rem',
                    backgroundColor: user.userType === 'READER' ? '#007bff' : user.userType === 'AUTHOR' ? '#28a745' : '#dc3545',
                    color: '#fff',
                    borderRadius: '20px',
                    cursor: 'pointer',
                    fontSize: '0.9rem'
                  }}
                >
                  <span style={{ fontSize: '1.2rem' }}>
                    {user.userType === 'READER' ? '👤' : user.userType === 'AUTHOR' ? '✍️' : '⚙️'}
                  </span>
                  <span>{user.name || user.email}</span>
                  <span style={{ fontSize: '0.8rem' }}>
                    ({user.userType === 'READER' ? '독자' : user.userType === 'AUTHOR' ? '작가' : '관리자'})
                  </span>
                </div>
                <button
                  onClick={handleLogout}
                  style={{
                    padding: '0.5rem 1rem',
                    backgroundColor: 'transparent',
                    border: '1px solid #dc3545',
                    borderRadius: '4px',
                    color: '#dc3545',
                    cursor: 'pointer',
                    fontSize: '0.9rem'
                  }}
                >
                  로그아웃
                </button>
              </div>
            ) : (
              <>
                <button
                  onClick={() => navigate('/login')}
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
                  로그인
                </button>

                <button
                  onClick={() => navigate('/register')}
                  style={{
                    padding: '0.5rem 1rem',
                    backgroundColor: '#333',
                    border: 'none',
                    borderRadius: '4px',
                    color: '#fff',
                    cursor: 'pointer',
                    fontSize: '0.9rem'
                  }}
                >
                  회원가입
                </button>
              </>
            )}
          </nav>
        </div>
      </header>

      {/* 메인 컨텐츠 */}
      <main style={{ maxWidth: '1200px', margin: '0 auto', padding: '2rem' }}>
        {/* 메인 히어로 섹션 */}
        <section style={{
          textAlign: 'center',
          marginBottom: '4rem',
          padding: '4rem 0'
        }}>
          <h2 style={{
            fontSize: '3rem',
            fontWeight: 'bold',
            color: '#333',
            marginBottom: '1rem'
          }}>
            새로운 이야기를 만나보세요
          </h2>
          <p style={{
            fontSize: '1.2rem',
            color: '#666',
            marginBottom: '2rem',
            lineHeight: '1.6'
          }}>
            작가와 독자가 만나는 특별한 공간<br/>
            당신만의 이야기를 공유하고, 새로운 세계를 경험해보세요
          </p>
          
          <div style={{
            maxWidth: '500px',
            margin: '0 auto 3rem',
            position: 'relative'
          }}>
            <input
              type="text"
              placeholder="책 제목이나 작가명을 검색하세요"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              style={{
                width: '100%',
                padding: '1rem 1.5rem',
                fontSize: '1rem',
                border: '2px solid #e5e5e5',
                borderRadius: '50px',
                outline: 'none',
                boxSizing: 'border-box'
              }}
            />
            <button 
              onClick={() => navigate('/bookListPage')}
              style={{
                position: 'absolute',
                right: '8px',
                top: '50%',
                transform: 'translateY(-50%)',
                backgroundColor: '#333',
                color: '#fff',
                border: 'none',
                borderRadius: '50%',
                width: '40px',
                height: '40px',
                cursor: 'pointer',
                fontSize: '1.2rem'
              }}
            >
              🔍
            </button>
          </div>

          <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center' }}>
            <button
              onClick={() => navigate('/bookListPage')}
              style={{
                padding: '1rem 2rem',
                backgroundColor: '#333',
                color: '#fff',
                border: 'none',
                borderRadius: '8px',
                fontSize: '1.1rem',
                fontWeight: 'bold',
                cursor: 'pointer'
              }}
            >
              도서 둘러보기
            </button>
            {!user && (
              <button
                onClick={() => navigate('/register')}
                style={{
                  padding: '1rem 2rem',
                  backgroundColor: 'transparent',
                  color: '#333',
                  border: '2px solid #333',
                  borderRadius: '8px',
                  fontSize: '1.1rem',
                  fontWeight: 'bold',
                  cursor: 'pointer'
                }}
              >
                지금 시작하기
              </button>
            )}
          </div>
        </section>

        {/* 구독 안내 */}
        {!user || user.userType === 'READER' ? (
          <section style={{
            backgroundColor: '#333',
            color: '#fff',
            padding: '2rem',
            borderRadius: '8px',
            marginBottom: '3rem',
            textAlign: 'center'
          }}>
            <h3 style={{ fontSize: '1.5rem', marginBottom: '1rem' }}>
              월 구독권으로 더 많은 책을 읽어보세요
            </h3>
            <p style={{ marginBottom: '1.5rem', color: '#ccc' }}>
              9,900 포인트로 한 달간 무제한 독서
            </p>
            <button 
              onClick={() => user ? navigate('/readerMypage') : navigate('/login')}
              style={{
                backgroundColor: '#fff',
                color: '#333',
                border: 'none',
                padding: '0.8rem 2rem',
                borderRadius: '4px',
                fontSize: '1rem',
                fontWeight: 'bold',
                cursor: 'pointer'
              }}
            >
              {user ? '구독하기' : '로그인 후 구독하기'}
            </button>
          </section>
        ) : null}

        {/* 작가 안내 */}
        {user && user.userType === 'AUTHOR' && (
          <section style={{
            backgroundColor: '#28a745',
            color: '#fff',
            padding: '2rem',
            borderRadius: '8px',
            marginBottom: '3rem',
            textAlign: 'center'
          }}>
            <h3 style={{ fontSize: '1.5rem', marginBottom: '1rem' }}>
              새로운 작품을 등록해보세요
            </h3>
            <p style={{ marginBottom: '1.5rem', color: '#e8f5e8' }}>
              독자들과 당신의 이야기를 공유하세요
            </p>
            <button 
              onClick={() => navigate('/authorMypage')}
              style={{
                backgroundColor: '#fff',
                color: '#28a745',
                border: 'none',
                padding: '0.8rem 2rem',
                borderRadius: '4px',
                fontSize: '1rem',
                fontWeight: 'bold',
                cursor: 'pointer'
              }}
            >
              작품 등록하기
            </button>
          </section>
        )}

        {/* BookHub 특징 소개 */}
        <section style={{ marginBottom: '4rem' }}>
          <h3 style={{
            fontSize: '2rem',
            color: '#333',
            textAlign: 'center',
            marginBottom: '3rem',
            fontWeight: 'bold'
          }}>
            왜 BookHub를 선택해야 할까요?
          </h3>
          
          <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))',
            gap: '2rem'
          }}>
            <div style={{
              backgroundColor: '#fff',
              padding: '2rem',
              borderRadius: '12px',
              boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
              textAlign: 'center'
            }}>
              <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>📚</div>
              <h4 style={{ fontSize: '1.3rem', color: '#333', marginBottom: '1rem' }}>
                다양한 창작물
              </h4>
              <p style={{ color: '#666', lineHeight: '1.6' }}>
                소설, 에세이, 시집까지 다양한 장르의 창작물을 만나보세요. 
                신인 작가부터 베테랑까지 모두의 이야기가 여기에 있습니다.
              </p>
            </div>

            <div style={{
              backgroundColor: '#fff',
              padding: '2rem',
              borderRadius: '12px',
              boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
              textAlign: 'center'
            }}>
              <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>💡</div>
              <h4 style={{ fontSize: '1.3rem', color: '#333', marginBottom: '1rem' }}>
                작가 지원 시스템
              </h4>
              <p style={{ color: '#666', lineHeight: '1.6' }}>
                작가들을 위한 수익 창출과 독자와의 직접적인 소통 기회를 제공합니다. 
                당신의 창작 활동을 적극 지원합니다.
              </p>
            </div>

            <div style={{
              backgroundColor: '#fff',
              padding: '2rem',
              borderRadius: '12px',
              boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
              textAlign: 'center'
            }}>
              <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>🎯</div>
              <h4 style={{ fontSize: '1.3rem', color: '#333', marginBottom: '1rem' }}>
                맞춤형 추천
              </h4>
              <p style={{ color: '#666', lineHeight: '1.6' }}>
                개인의 취향을 분석하여 맞춤형 도서를 추천합니다. 
                새로운 작가와 장르를 발견하는 즐거움을 경험해보세요.
              </p>
            </div>
          </div>
        </section>

        {/* 통계 섹션 */}
        <section style={{
          backgroundColor: '#333',
          color: '#fff',
          padding: '3rem 2rem',
          borderRadius: '12px',
          marginBottom: '4rem',
          textAlign: 'center'
        }}>
          <h3 style={{
            fontSize: '2rem',
            marginBottom: '2rem',
            fontWeight: 'bold'
          }}>
            BookHub와 함께하는 사람들
          </h3>
          
          <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))',
            gap: '2rem'
          }}>
            <div>
              <div style={{ fontSize: '2.5rem', fontWeight: 'bold', marginBottom: '0.5rem' }}>
                1,200+
              </div>
              <div style={{ color: '#ccc' }}>등록된 작품</div>
            </div>
            <div>
              <div style={{ fontSize: '2.5rem', fontWeight: 'bold', marginBottom: '0.5rem' }}>
                850+
              </div>
              <div style={{ color: '#ccc' }}>활동 작가</div>
            </div>
            <div>
              <div style={{ fontSize: '2.5rem', fontWeight: 'bold', marginBottom: '0.5rem' }}>
                15,000+
              </div>
              <div style={{ color: '#ccc' }}>독자 회원</div>
            </div>
            <div>
              <div style={{ fontSize: '2.5rem', fontWeight: 'bold', marginBottom: '0.5rem' }}>
                98%
              </div>
              <div style={{ color: '#ccc' }}>만족도</div>
            </div>
          </div>
        </section>
      </main>

      {/* 푸터 */}
      <footer style={{
        backgroundColor: '#333',
        color: '#fff',
        padding: '2rem 0',
        marginTop: '4rem'
      }}>
        <div style={{
          maxWidth: '1200px',
          margin: '0 auto',
          padding: '0 2rem',
          textAlign: 'center'
        }}>
          <h3 style={{ marginBottom: '1rem' }}>BookHub</h3>
          <p style={{ color: '#ccc' }}>
            새로운 이야기와 함께하는 독서 플랫폼
          </p>
        </div>
      </footer>
    </div>
  );
};

export default MainPage;