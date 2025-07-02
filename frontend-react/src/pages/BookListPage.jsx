import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { bookService } from '../bookService';

const BookListPage = () => {
  const navigate = useNavigate();
  
  // 도서 목록 상태
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('전체');
  const [sortBy, setSortBy] = useState('latest'); // latest, popular, price-low, price-high
  
  // 임시 사용자 상태 (실제로는 context에서 가져올 것)
  const [user, setUser] = useState(null); // null, { type: 'reader', name: '홍길동' }

  // 카테고리 목록
  const categories = ['전체', '소설', 'SF', '로맨스', '에세이', '역사', '자기계발'];

  // 임시 도서 데이터 (실제로는 API에서 가져올 것)
  const sampleBooks = [
    {
      id: 1,
      title: '별을 삼킨 소년',
      author: '김작가',
      category: 'SF',
      price: 5000,
      imageUrl: 'https://via.placeholder.com/200x280/333333/ffffff?text=별을+삼킨+소년',
      description: '소년은 어느 조용한 밤, 마당에 떨어진 작은 별 하나를 발견했다...',
      publishDate: '2025-06-28',
      views: 1250,
      rating: 4.7,
      reviewCount: 89
    },
    {
      id: 2,
      title: '미래의 기억',
      author: '이작가',
      category: 'SF',
      price: 4500,
      imageUrl: 'https://via.placeholder.com/200x280/444444/ffffff?text=미래의+기억',
      description: '시간을 거슬러 올라가는 기억에 관한 이야기...',
      publishDate: '2025-06-25',
      views: 980,
      rating: 4.5,
      reviewCount: 67
    },
    {
      id: 3,
      title: '도시의 밤',
      author: '박작가',
      category: '소설',
      price: 5500,
      imageUrl: 'https://via.placeholder.com/200x280/555555/ffffff?text=도시의+밤',
      description: '도시에서 살아가는 사람들의 이야기...',
      publishDate: '2025-06-20',
      views: 1450,
      rating: 4.8,
      reviewCount: 123
    },
    {
      id: 4,
      title: '바람의 노래',
      author: '최작가',
      category: '에세이',
      price: 4000,
      imageUrl: 'https://via.placeholder.com/200x280/666666/ffffff?text=바람의+노래',
      description: '자연과 인간의 관계를 다룬 에세이...',
      publishDate: '2025-06-15',
      views: 720,
      rating: 4.3,
      reviewCount: 45
    },
    {
      id: 5,
      title: '시간의 틈',
      author: '정작가',
      category: 'SF',
      price: 4800,
      imageUrl: 'https://via.placeholder.com/200x280/777777/ffffff?text=시간의+틈',
      description: '시간 여행을 소재로 한 SF 소설...',
      publishDate: '2025-06-10',
      views: 890,
      rating: 4.6,
      reviewCount: 78
    },
    {
      id: 6,
      title: '별빛 여행',
      author: '강작가',
      category: '로맨스',
      price: 5200,
      imageUrl: 'https://via.placeholder.com/200x280/888888/ffffff?text=별빛+여행',
      description: '별빛 아래서 펼쳐지는 로맨스 이야기...',
      publishDate: '2025-06-05',
      views: 1100,
      rating: 4.4,
      reviewCount: 92
    }
  ];

  // 도서 목록 불러오기
  useEffect(() => {
    const loadBooks = async () => {
      setLoading(true);
      try {
        // 실제로는 API 호출
        const books = await bookService.getBooks();
        
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
      const matchesSearch = book.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
                           book.author.toLowerCase().includes(searchTerm.toLowerCase());
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

  // 도서 구매
  const handlePurchase = async (book) => {
    if (!user) {
      alert('로그인이 필요합니다.');
      navigate('/login');
      return;
    }

    if (user.type !== 'reader') {
      alert('독자만 도서를 구매할 수 있습니다.');
      return;
    }

    // 실제로는 구매 API 호출
    const confirmed = window.confirm(`"${book.title}"을(를) ${book.price.toLocaleString()}P에 구매하시겠습니까?`);
    if (confirmed) {
      try {
        // 구매 API 호출
        alert('구매가 완료되었습니다!');
      } catch (error) {
        alert('구매 중 오류가 발생했습니다.');
      }
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
          
          <nav style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
            {user ? (
              <>
                <span style={{ color: '#666', fontSize: '0.9rem' }}>
                  안녕하세요, {user.name}님
                </span>
                {user.type === 'reader' && (
                  <button
                    onClick={() => navigate('/reader-mypage')}
                    style={{
                      padding: '0.5rem 1rem',
                      backgroundColor: '#007bff',
                      border: 'none',
                      borderRadius: '4px',
                      color: '#fff',
                      cursor: 'pointer',
                      fontSize: '0.9rem'
                    }}
                  >
                    마이페이지
                  </button>
                )}
                {user.type === 'author' && (
                  <button
                    onClick={() => navigate('/author-mypage')}
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
                    작가 페이지
                  </button>
                )}
                <button
                  onClick={() => setUser(null)}
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
              </>
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
            
            {/* 테스트용 로그인 */}
            <div style={{ display: 'flex', gap: '0.5rem' }}>
              <button
                onClick={() => setUser({ type: 'reader', name: '홍길동' })}
                style={{
                  padding: '0.3rem 0.6rem',
                  backgroundColor: '#007bff',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '3px',
                  cursor: 'pointer',
                  fontSize: '0.7rem'
                }}
              >
                독자
              </button>
              <button
                onClick={() => setUser({ type: 'author', name: '김작가' })}
                style={{
                  padding: '0.3rem 0.6rem',
                  backgroundColor: '#28a745',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '3px',
                  cursor: 'pointer',
                  fontSize: '0.7rem'
                }}
              >
                작가
              </button>
            </div>
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
              onClick={() => navigate(`/books/${book.id}`)}
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
                    by {book.author}
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
