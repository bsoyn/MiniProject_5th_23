import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { bookService } from '../bookService.jsx';

const BASE_URL = process.env.REACT_APP_API_BASE_URL;

const BookListPage = () => {
  const navigate = useNavigate();
  
  // 도서 목록 상태
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('전체');
  const [sortBy, setSortBy] = useState('latest'); // latest, popular, price-low, price-high
  
  // 사용자 상태 - 세션 스토리지에서 읽어오기
  const [user, setUser] = useState(null);

  // 로그아웃 처리
  const handleLogout = () => {
    // 세션 스토리지 클리어
    sessionStorage.removeItem('userInfo');
    sessionStorage.removeItem('accessToken');
    setUser(null);
    navigate('/');
  };

  // 카테고리 목록
  const categories = ['전체', '소설', 'SF', '로맨스', '에세이', '역사', '자기계발'];

  // 토큰으로 사용자 정보 가져오기
  useEffect(() => {
    const loadUserInfo = async () => {
      const userInfo = sessionStorage.getItem('userInfo');
      const accessToken = sessionStorage.getItem('accessToken');
      
      console.log('세션 스토리지 userInfo:', userInfo); // 디버깅용
      
      if (!accessToken) {
        console.log('토큰이 없어서 로그인 없이 진행');
        return;
      }

      try {
        // 토큰으로 사용자 정보 가져오기
        const response = await fetch(`${BASE_URL}/api/token`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json',
          },
        });

        if (!response.ok) {
          throw new Error('사용자 정보를 가져올 수 없습니다');
        }

        const userData = await response.json();
        console.log('API에서 받은 사용자 정보:', userData); // 디버깅용

        // 세션 스토리지의 userType과 API 응답 조합
        let userType = 'reader'; // 기본값
        let email = '';
        if (userInfo) {
          try {
            const parsedUser = JSON.parse(userInfo);
            userType = parsedUser.userType?.toLowerCase() || 'reader';
            email = parsedUser.email || '';
          } catch (error) {
            console.warn('세션 스토리지 파싱 실패, 기본값 사용');
          }
        }

        const finalUserData = {
          id: userData.userId,
          type: userType,
          name: userData.userName,
          email: email
        };
        
        console.log('최종 user 상태:', finalUserData); // 디버깅용
        setUser(finalUserData);

      } catch (error) {
        console.error('사용자 정보 로딩 실패:', error);
        // 토큰이 유효하지 않으면 세션 스토리지 클리어
        sessionStorage.removeItem('userInfo');
        sessionStorage.removeItem('accessToken');
        setUser(null);
      }
    };

    loadUserInfo();
  }, []);

  // 도서 목록 불러오기
  useEffect(() => {
    const loadBooks = async () => {
      setLoading(true);
      try {
        // 실제로는 API 호출
        const books = await bookService.getBooks(0);
        console.log("응답 확인 : ", books[0]);
        
        // 임시로 샘플 데이터 사용
        //await new Promise(resolve => setTimeout(resolve, 1000)); // 로딩 시뮬레이션
        setBooks(books);
      } catch (error) {
        console.error('도서 목록 불러오기 실패:', error);
      } finally {
        setLoading(false);
      }
    };

    loadBooks();
  }, []);

  // 필터링 및 정렬된 도서 목록
  const filteredAndSortedBooks = books
    .filter(book => {
      const matchesSearch = book.title.toLowerCase().includes(searchTerm.toLowerCase())
      const matchesCategory = selectedCategory === '전체' || book.category === selectedCategory;
      return matchesSearch && matchesCategory;
    })
    .sort((a, b) => {
      switch (sortBy) {
        case 'latest':
          return new Date(b.publishDate) - new Date(a.publishDate);
        case 'popular':
          return b.views - a.views;
        case 'price-low':
          return a.price - b.price;
        case 'price-high':
          return b.price - a.price;
        default:
          return 0;
      }
    });

  // 도서 구매 - /bookPurchase로 네비게이트
  const handlePurchase = async (book) => {
    console.log('현재 user 상태:', user); // 디버깅용
    
    if (!user) {
      alert('로그인이 필요합니다.');
      navigate('/login');
      return;
    }

    if (user.type !== 'reader') {
      alert('독자만 도서를 구매할 수 있습니다.');
      return;
    }

    // bookId만 path parameter로 전달 (userId는 BookPurchasePage에서 세션 스토리지로 가져옴)
    navigate(`/bookPurchase/${book.id}`);
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
            {/* 홈으로 가기 버튼 */}
            <button
              onClick={() => navigate('/')}
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
              홈으로
            </button>

            {/* 로그인 상태에 따른 조건부 렌더링 */}
            {user ? (
              <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                <div 
                  onClick={() => {
                    if (user.type === 'reader') {
                      navigate('/readerMypage');
                    } else if (user.type === 'author') {
                      navigate('/authorMypage');
                    } else if (user.type === 'admin') {
                      navigate('/admin');
                    }
                  }}
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '0.5rem',
                    padding: '0.5rem 1rem',
                    backgroundColor: user.type === 'reader' ? '#007bff' : user.type === 'author' ? '#28a745' : '#dc3545',
                    color: '#fff',
                    borderRadius: '20px',
                    cursor: 'pointer',
                    fontSize: '0.9rem'
                  }}
                >
                  <span style={{ fontSize: '1.2rem' }}>
                    {user.type === 'reader' ? '👤' : user.type === 'author' ? '✍️' : '⚙️'}
                  </span>
                  <span>{user.name || user.email}</span>
                  <span style={{ fontSize: '0.8rem' }}>
                    ({user.type === 'reader' ? '독자' : user.type === 'author' ? '작가' : '관리자'})
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

      <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '2rem' }}>
        {/* 페이지 제목 */}
        <h2 style={{
          fontSize: '2rem',
          fontWeight: 'bold',
          color: '#333',
          marginBottom: '2rem'
        }}>
          도서 목록
        </h2>

        {/* 검색 및 필터 섹션 */}
        <div style={{
          backgroundColor: '#fff',
          borderRadius: '8px',
          padding: '1.5rem',
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
          marginBottom: '2rem'
        }}>
          {/* 검색바 */}
          <div style={{ marginBottom: '1.5rem' }}>
            <div style={{
              maxWidth: '500px',
              position: 'relative'
            }}>
              <input
                type="text"
                placeholder="책 제목이나 작가명을 검색하세요"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                style={{
                  width: '100%',
                  padding: '0.8rem 3rem 0.8rem 1rem',
                  fontSize: '1rem',
                  border: '2px solid #e5e5e5',
                  borderRadius: '25px',
                  outline: 'none',
                  boxSizing: 'border-box'
                }}
              />
              <button style={{
                position: 'absolute',
                right: '8px',
                top: '50%',
                transform: 'translateY(-50%)',
                backgroundColor: 'transparent',
                border: 'none',
                cursor: 'pointer',
                fontSize: '1.2rem'
              }}>
                🔍
              </button>
            </div>
          </div>

          {/* 필터 및 정렬 */}
          <div style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            flexWrap: 'wrap',
            gap: '1rem'
          }}>
            {/* 카테고리 필터 */}
            <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
              {categories.map(category => (
                <button
                  key={category}
                  onClick={() => setSelectedCategory(category)}
                  style={{
                    padding: '0.5rem 1rem',
                    backgroundColor: selectedCategory === category ? '#333' : 'transparent',
                    color: selectedCategory === category ? '#fff' : '#666',
                    border: '1px solid #ddd',
                    borderRadius: '20px',
                    cursor: 'pointer',
                    fontSize: '0.9rem',
                    transition: 'all 0.2s ease'
                  }}
                >
                  {category}
                </button>
              ))}
            </div>

            {/* 정렬 옵션 */}
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              style={{
                padding: '0.5rem 1rem',
                border: '1px solid #ddd',
                borderRadius: '4px',
                fontSize: '0.9rem',
                outline: 'none'
              }}
            >
              <option value="latest">최신순</option>
              <option value="popular">인기순</option>
              <option value="price-low">가격 낮은순</option>
              <option value="price-high">가격 높은순</option>
            </select>
          </div>
        </div>

        {/* 결과 개수 */}
        <div style={{
          marginBottom: '1.5rem',
          color: '#666'
        }}>
          총 {filteredAndSortedBooks.length}권의 도서
        </div>

        {/* 도서 목록 */}
        {loading ? (
          <div style={{
            textAlign: 'center',
            padding: '3rem',
            color: '#666'
          }}>
            <div style={{ fontSize: '2rem', marginBottom: '1rem' }}>📚</div>
            <p>도서 목록을 불러오는 중...</p>
          </div>
        ) : filteredAndSortedBooks.length > 0 ? (
          <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))',
            gap: '1.5rem'
          }}>
            {filteredAndSortedBooks.map(book => (
              <div key={book.id} style={{
                backgroundColor: '#fff',
                borderRadius: '8px',
                overflow: 'hidden',
                boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
                transition: 'transform 0.2s ease',
                cursor: 'pointer'
              }}
              onMouseOver={(e) => e.currentTarget.style.transform = 'translateY(-4px)'}
              onMouseOut={(e) => e.currentTarget.style.transform = 'translateY(0)'}
              onClick={() => navigate(`/book-detail/${book.id}`)}
              >
                {/* 도서 이미지 */}
                <div style={{
                  width: '100%',
                  height: '200px',
                  backgroundImage: `url(${book.imageUrl})`,
                  backgroundSize: 'cover',
                  backgroundPosition: 'center',
                  backgroundColor: '#f0f0f0'
                }} />

                {/* 도서 정보 */}
                <div style={{ padding: '1.5rem' }}>
                  <div style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'flex-start',
                    marginBottom: '0.5rem'
                  }}>
                    <h3 style={{
                      fontSize: '1.2rem',
                      fontWeight: 'bold',
                      color: '#333',
                      margin: 0,
                      flex: 1
                    }}>
                      {book.title}
                    </h3>
                    <div style={{
                      backgroundColor: '#f0f0f0',
                      color: '#666',
                      padding: '0.2rem 0.6rem',
                      borderRadius: '12px',
                      fontSize: '0.8rem',
                      marginLeft: '0.5rem'
                    }}>
                      {book.category}
                    </div>
                  </div>

                  <p style={{
                    color: '#666',
                    marginBottom: '0.5rem',
                    fontSize: '0.9rem'
                  }}>
                    by {book.authorName}
                  </p>

                  <p style={{
                    color: '#666',
                    marginBottom: '1rem',
                    fontSize: '0.9rem',
                    lineHeight: '1.4',
                    overflow: 'hidden',
                    display: '-webkit-box',
                    WebkitLineClamp: 2,
                    WebkitBoxOrient: 'vertical'
                  }}>
                    {book.description}
                  </p>

                  {/* 평점 및 통계 */}
                  <div style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    marginBottom: '1rem',
                    fontSize: '0.8rem',
                    color: '#999'
                  }}>
                    <div>
                      조회 {book.views.toLocaleString()}
                    </div>
                  </div>

                  {/* 가격 및 구매 버튼 */}
                  <div style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center'
                  }}>
                    <span style={{
                      fontSize: '1.3rem',
                      fontWeight: 'bold',
                      color: '#333'
                    }}>
                      {book.price.toLocaleString()}P
                    </span>
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        handlePurchase(book);
                      }}
                      style={{
                        padding: '0.6rem 1.2rem',
                        backgroundColor: '#007bff',
                        color: '#fff',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer',
                        fontSize: '0.9rem',
                        fontWeight: '500'
                      }}
                    >
                      구매하기
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
            <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>📚</div>
            <p>검색 조건에 맞는 도서가 없습니다.</p>
            <button
              onClick={() => {
                setSearchTerm('');
                setSelectedCategory('전체');
              }}
              style={{
                marginTop: '1rem',
                padding: '0.8rem 1.5rem',
                backgroundColor: '#007bff',
                color: '#fff',
                border: 'none',
                borderRadius: '4px',
                cursor: 'pointer'
              }}
            >
              전체 도서 보기
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default BookListPage;