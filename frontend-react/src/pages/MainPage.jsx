import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { purchaseBook } from '../purchasebook_api/purchasebook';

const MainPage = () => {
    const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState('');
  
  // 임시 로그인 상태 (실제로는 context나 redux에서 관리)
  const [user, setUser] = useState(null); // null, { type: 'reader', name: '홍길동' }, { type: 'author', name: '김작가' }

  // 샘플 도서 데이터
  const books = [
    { id: 1, title: '미래의 기억', author: '김작가', price: 5000, image: '📚' },
    { id: 2, title: '도시의 밤', author: '이작가', price: 4500, image: '🌃' },
    { id: 3, title: '바람의 노래', author: '박작가', price: 5500, image: '🎵' },
    { id: 4, title: '시간의 틈', author: '최작가', price: 4800, image: '⏰' },
    { id: 5, title: '별빛 여행', author: '정작가', price: 5200, image: '⭐' },
    { id: 6, title: '물의 기록', author: '강작가', price: 4700, image: '💧' }
  ];

  const filteredBooks = books.filter(book =>
    book.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
    book.author.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handlePurchase = async (bookId) => {
    if (!user || user.type !== 'reader') {
      alert('구매하려면 먼저 독자로 로그인하세요.');
      return;
    }

    try {
      const result = await purchaseBook(1, bookId); // 테스트용 readerId
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
          <h1 style={{
            fontSize: '1.8rem',
            fontWeight: 'bold',
            color: '#333',
            margin: 0
          }}>
            BookHub
          </h1>
          
          <nav style={{ display: 'flex', gap: '1rem' }}>
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
          </nav>
        </div>
      </header>

      {/* 메인 컨텐츠 */}
      <main style={{ maxWidth: '1200px', margin: '0 auto', padding: '2rem' }}>
        {/* 검색 섹션 */}
        <section style={{
          textAlign: 'center',
          marginBottom: '3rem',
          padding: '3rem 0'
        }}>
          <h2 style={{
            fontSize: '2.5rem',
            fontWeight: 'bold',
            color: '#333',
            marginBottom: '1rem'
          }}>
            새로운 이야기를 만나보세요
          </h2>
          <p style={{
            fontSize: '1.1rem',
            color: '#666',
            marginBottom: '2rem'
          }}>
            다양한 작가들의 창작물을 읽고, 당신만의 이야기를 공유하세요
          </p>
          
          <div style={{
            maxWidth: '500px',
            margin: '0 auto',
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
            <button style={{
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
            }}>
              🔍
            </button>
          </div>
        </section>

        {/* 구독 안내 */}
        {!user || user.type === 'reader' ? (
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
              onClick={() => user ? navigate('/reader-mypage') : navigate('/login')}
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
        {user && user.type === 'author' && (
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
              onClick={() => navigate('/author-mypage')}
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

        {/* 임시 로그인 테스트 버튼들 */}
        <div style={{ 
          textAlign: 'center', 
          marginBottom: '2rem',
          padding: '1rem',
          backgroundColor: '#fff',
          borderRadius: '8px',
          boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
        }}>
          <p style={{ color: '#666', marginBottom: '1rem', fontSize: '0.9rem' }}>
            테스트용 로그인 (실제 서비스에서는 제거)
          </p>
          <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center' }}>
            <button
              onClick={() => setUser({ type: 'reader', name: '홍길동' })}
              style={{
                padding: '0.5rem 1rem',
                backgroundColor: '#007bff',
                color: '#fff',
                border: 'none',
                borderRadius: '4px',
                cursor: 'pointer',
                fontSize: '0.8rem'
              }}
            >
              독자로 로그인
            </button>
            <button
              onClick={() => setUser({ type: 'author', name: '김작가' })}
              style={{
                padding: '0.5rem 1rem',
                backgroundColor: '#28a745',
                color: '#fff',
                border: 'none',
                borderRadius: '4px',
                cursor: 'pointer',
                fontSize: '0.8rem'
              }}
            >
              작가로 로그인
            </button>
            <button
              onClick={() => setUser(null)}
              style={{
                padding: '0.5rem 1rem',
                backgroundColor: '#6c757d',
                color: '#fff',
                border: 'none',
                borderRadius: '4px',
                cursor: 'pointer',
                fontSize: '0.8rem'
              }}
            >
              로그아웃
            </button>
          </div>
        </div>

        {/* 도서 목록 */}
        <section>
          <h3 style={{
            fontSize: '1.8rem',
            color: '#333',
            marginBottom: '2rem',
            fontWeight: 'bold'
          }}>
            추천 도서
          </h3>
          
          <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
            gap: '1.5rem'
          }}>
            {filteredBooks.map(book => (
              <div key={book.id} style={{
                backgroundColor: '#fff',
                borderRadius: '8px',
                padding: '1.5rem',
                boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
                transition: 'transform 0.2s ease',
                cursor: 'pointer'
              }}
              onMouseOver={(e) => e.currentTarget.style.transform = 'translateY(-2px)'}
              onMouseOut={(e) => e.currentTarget.style.transform = 'translateY(0)'}
              >
                <div style={{
                  fontSize: '3rem',
                  textAlign: 'center',
                  marginBottom: '1rem'
                }}>
                  {book.image}
                </div>
                <h4 style={{
                  fontSize: '1.2rem',
                  fontWeight: 'bold',
                  color: '#333',
                  marginBottom: '0.5rem'
                }}>
                  {book.title}
                </h4>
                <p style={{
                  color: '#666',
                  marginBottom: '1rem'
                }}>
                  by {book.author}
                </p>
                <div style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center'
                }}>
                  <span style={{
                    fontSize: '1.1rem',
                    fontWeight: 'bold',
                    color: '#333'
                  }}>
                    {book.price.toLocaleString()}P
                  </span>
                  <button 
                    onClick={() => handlePurchase(book.id)}
                    style={{
                      backgroundColor: '#333',
                      color: '#fff',
                      border: 'none',
                      padding: '0.5rem 1rem',
                      borderRadius: '4px',
                      cursor: 'pointer',
                      fontSize: '0.9rem'
                    }}
                  >
                    구매하기
                  </button>
                </div>
              </div>
            ))}
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