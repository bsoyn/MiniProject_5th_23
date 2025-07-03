import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

const BookDetailPage = () => {
  const navigate = useNavigate();
  const { bookId } = useParams();
  
  // 상태 관리
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);
  const [user, setUser] = useState(null); // 임시 사용자 상태
  const [isPurchased, setIsPurchased] = useState(false);
  const [showPurchaseModal, setShowPurchaseModal] = useState(false);
  const [reviews, setReviews] = useState([]);
  const [newReview, setNewReview] = useState({ rating: 5, content: '' });

  // 임시 도서 데이터
  const sampleBook = {
    id: parseInt(bookId),
    title: '별을 삼킨 소년',
    author: '김작가',
    authorId: 1,
    category: 'SF',
    price: 5000,
    imageUrl: 'https://via.placeholder.com/400x560/333333/ffffff?text=별을+삼킨+소년',
    fullContent: `소년은 어느 조용한 밤, 마당에 떨어진 작은 별 하나를 발견했다. 손바닥 위에서 희미하게 빛나던 그 별은 말없이 소년의 마음속 깊은 곳을 비췄다. 

소년은 별을 삼켰고, 그날부터 세상이 달라 보이기 시작했다. 사람들의 말은 빛으로 들렸고, 바람은 감정을 품고 불어왔다. 매일 밤, 그는 별의 기억을 따라 낯선 풍경 속으로 걸어 들어갔다.

그리고 마침내, 별을 통해 이어진 세계가 단지 환상이 아니라 누군가의 기억이라는 것을 깨닫게 된다. 소년은 별 속에 담긴 이야기들을 하나씩 풀어가며, 자신만의 우주를 만들어간다.

이 소설은 현실과 환상의 경계를 넘나들며, 성장과 발견의 의미를 탐구한다. 독자들은 소년과 함께 신비로운 여행을 떠나며, 내면의 별을 발견하게 될 것이다.`,
    description: '소년은 어느 조용한 밤, 마당에 떨어진 작은 별 하나를 발견했다. 손바닥 위에서 희미하게 빛나던 그 별은 말없이 소년의 마음속 깊은 곳을 비췄다.',
    publishDate: '2025-06-28',
    views: 1250,
    rating: 4.7,
    reviewCount: 89,
    pages: 180,
    language: '한국어',
    isbn: '979-11-1234-567-8'
  };

  // 임시 리뷰 데이터
  const sampleReviews = [
    {
      id: 1,
      userId: 'reader1',
      userName: '독서광',
      rating: 5,
      content: '정말 감동적인 이야기였습니다. 소년의 성장 과정이 아름답게 그려져 있어요.',
      createdAt: '2025-06-25',
      helpful: 15
    },
    {
      id: 2,
      userId: 'reader2',
      userName: '책벌레',
      rating: 4,
      content: '환상적인 세계관과 섬세한 문체가 인상적이었습니다. 마지막 부분이 특히 좋았어요.',
      createdAt: '2025-06-20',
      helpful: 8
    },
    {
      id: 3,
      userId: 'reader3',
      userName: '문학애호가',
      rating: 5,
      content: 'SF 장르를 좋아하는데 이런 서정적인 SF는 처음이네요. 강추합니다!',
      createdAt: '2025-06-15',
      helpful: 12
    }
  ];

  // 도서 정보 불러오기
  useEffect(() => {
    const loadBook = async () => {
      setLoading(true);
      try {
        // 실제로는 API 호출
        // const response = await fetch(`https://8088.../books/${bookId}`);
        // const data = await response.json();
        
        await new Promise(resolve => setTimeout(resolve, 1000));
        setBook(sampleBook);
        setReviews(sampleReviews);
        
        // 구매 여부 확인 (실제로는 API 호출)
        // setIsPurchased(checkIfPurchased());
      } catch (error) {
        console.error('도서 정보 불러오기 실패:', error);
      } finally {
        setLoading(false);
      }
    };

    loadBook();
  }, [bookId]);

  // 도서 구매
  const handlePurchase = async () => {
    if (!user) {
      alert('로그인이 필요합니다.');
      navigate('/login');
      return;
    }

    if (user.type !== 'reader') {
      alert('독자만 도서를 구매할 수 있습니다.');
      return;
    }

    try {
      // 실제로는 구매 API 호출
      // const response = await fetch('/api/purchase', { ... });
      
      setIsPurchased(true);
      setShowPurchaseModal(false);
      alert('구매가 완료되었습니다! 이제 도서를 읽을 수 있습니다.');
    } catch (error) {
      alert('구매 중 오류가 발생했습니다.');
    }
  };

  // 리뷰 작성
  const handleReviewSubmit = async (e) => {
    e.preventDefault();
    
    if (!user) {
      alert('로그인이 필요합니다.');
      return;
    }

    if (!isPurchased) {
      alert('구매한 도서만 리뷰를 작성할 수 있습니다.');
      return;
    }

    if (!newReview.content.trim()) {
      alert('리뷰 작성 중 오류가 발생했습니다.');
    }
  };

  // 도서 읽기
  const handleReadBook = () => {
    if (!isPurchased) {
      alert('구매한 도서만 읽을 수 있습니다.');
      return;
    }
    // 실제로는 독서 페이지로 이동
    alert('독서 페이지로 이동합니다. (개발 예정)');
  };

  if (loading) {
    return (
      <div style={{
        minHeight: '100vh',
        backgroundColor: '#f8f9fa',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center'
      }}>
        <div style={{ textAlign: 'center', color: '#666' }}>
          <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>📚</div>
          <p>도서 정보를 불러오는 중...</p>
        </div>
      </div>
    );
  }

  if (!book) {
    return (
      <div style={{
        minHeight: '100vh',
        backgroundColor: '#f8f9fa',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center'
      }}>
        <div style={{ textAlign: 'center', color: '#666' }}>
          <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>❌</div>
          <p>도서를 찾을 수 없습니다.</p>
          <button
            onClick={() => navigate('/books')}
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
            도서 목록으로 돌아가기
          </button>
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
          
          <nav style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
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
                독자로그인
              </button>
              <button
                onClick={() => setUser(null)}
                style={{
                  padding: '0.3rem 0.6rem',
                  backgroundColor: '#6c757d',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '3px',
                  cursor: 'pointer',
                  fontSize: '0.7rem'
                }}
              >
                로그아웃
              </button>
            </div>
          </nav>
        </div>
      </header>

      <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '2rem' }}>
        {/* 뒤로가기 버튼 */}
        <button
          onClick={() => navigate('/books')}
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem',
            padding: '0.5rem 1rem',
            backgroundColor: 'transparent',
            border: '1px solid #ddd',
            borderRadius: '4px',
            color: '#666',
            cursor: 'pointer',
            fontSize: '0.9rem',
            marginBottom: '2rem'
          }}
        >
          ← 도서 목록으로
        </button>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: '3rem' }}>
          {/* 왼쪽: 도서 이미지 및 기본 정보 */}
          <div>
            <div style={{
              backgroundColor: '#fff',
              borderRadius: '8px',
              padding: '2rem',
              boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              position: 'sticky',
              top: '2rem'
            }}>
              {/* 도서 이미지 */}
              <div style={{
                width: '100%',
                height: '400px',
                backgroundImage: `url(${book.imageUrl})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                backgroundColor: '#f0f0f0',
                borderRadius: '8px',
                marginBottom: '1.5rem'
              }} />

              {/* 구매/읽기 버튼 */}
              <div style={{ marginBottom: '1.5rem' }}>
                {isPurchased ? (
                  <button
                    onClick={handleReadBook}
                    style={{
                      width: '100%',
                      padding: '1rem',
                      backgroundColor: '#28a745',
                      color: '#fff',
                      border: 'none',
                      borderRadius: '8px',
                      cursor: 'pointer',
                      fontSize: '1.1rem',
                      fontWeight: 'bold',
                      marginBottom: '0.5rem'
                    }}
                  >
                    📖 읽기 시작
                  </button>
                ) : (
                  <button
                    onClick={() => setShowPurchaseModal(true)}
                    style={{
                      width: '100%',
                      padding: '1rem',
                      backgroundColor: '#007bff',
                      color: '#fff',
                      border: 'none',
                      borderRadius: '8px',
                      cursor: 'pointer',
                      fontSize: '1.1rem',
                      fontWeight: 'bold',
                      marginBottom: '0.5rem'
                    }}
                  >
                    💰 {book.price.toLocaleString()}P로 구매
                  </button>
                )}
                
                <div style={{
                  textAlign: 'center',
                  fontSize: '0.8rem',
                  color: '#666'
                }}>
                  {isPurchased ? '구매 완료' : '구매 후 바로 읽을 수 있습니다'}
                </div>
              </div>

              {/* 도서 기본 정보 */}
              <div style={{
                padding: '1rem',
                backgroundColor: '#f8f9fa',
                borderRadius: '8px'
              }}>
                <h4 style={{ color: '#333', marginBottom: '1rem' }}>도서 정보</h4>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span style={{ color: '#666' }}>카테고리</span>
                    <span style={{ fontWeight: '500' }}>{book.category}</span>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span style={{ color: '#666' }}>페이지</span>
                    <span style={{ fontWeight: '500' }}>{book.pages}쪽</span>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span style={{ color: '#666' }}>언어</span>
                    <span style={{ fontWeight: '500' }}>{book.language}</span>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span style={{ color: '#666' }}>출간일</span>
                    <span style={{ fontWeight: '500' }}>{new Date(book.publishDate).toLocaleDateString()}</span>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span style={{ color: '#666' }}>ISBN</span>
                    <span style={{ fontWeight: '500', fontSize: '0.8rem' }}>{book.isbn}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* 오른쪽: 상세 정보 */}
          <div>
            {/* 제목 및 작가 정보 */}
            <div style={{
              backgroundColor: '#fff',
              borderRadius: '8px',
              padding: '2rem',
              boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              marginBottom: '2rem'
            }}>
              <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'flex-start',
                marginBottom: '1rem'
              }}>
                <h1 style={{
                  fontSize: '2.2rem',
                  fontWeight: 'bold',
                  color: '#333',
                  margin: 0,
                  flex: 1
                }}>
                  {book.title}
                </h1>
                <div style={{
                  backgroundColor: '#f0f0f0',
                  color: '#666',
                  padding: '0.5rem 1rem',
                  borderRadius: '20px',
                  fontSize: '0.9rem',
                  marginLeft: '1rem'
                }}>
                  {book.category}
                </div>
              </div>

              <p style={{
                fontSize: '1.2rem',
                color: '#666',
                marginBottom: '1.5rem'
              }}>
                by {book.author}
              </p>

              {/* 평점 및 통계 */}
              <div style={{
                display: 'flex',
                gap: '2rem',
                marginBottom: '1.5rem',
                padding: '1rem',
                backgroundColor: '#f8f9fa',
                borderRadius: '8px'
              }}>
                <div style={{ textAlign: 'center' }}>
                  <div style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#333' }}>
                    ⭐ {book.rating}
                  </div>
                  <div style={{ fontSize: '0.8rem', color: '#666' }}>평점</div>
                </div>
                <div style={{ textAlign: 'center' }}>
                  <div style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#333' }}>
                    {book.reviewCount}
                  </div>
                  <div style={{ fontSize: '0.8rem', color: '#666' }}>리뷰</div>
                </div>
                <div style={{ textAlign: 'center' }}>
                  <div style={{ fontSize: '1.5rem', fontWeight: 'bold', color: '#333' }}>
                    {book.views.toLocaleString()}
                  </div>
                  <div style={{ fontSize: '0.8rem', color: '#666' }}>조회</div>
                </div>
              </div>

              {/* 도서 설명 */}
              <h3 style={{ color: '#333', marginBottom: '1rem' }}>작품 소개</h3>
              <p style={{
                color: '#666',
                lineHeight: '1.6',
                marginBottom: '1.5rem'
              }}>
                {book.description}
              </p>

              {/* 전체 내용 (구매한 경우만 표시) */}
              {isPurchased && (
                <div>
                  <h3 style={{ color: '#333', marginBottom: '1rem' }}>전체 내용</h3>
                  <div style={{
                    padding: '1.5rem',
                    backgroundColor: '#f8f9fa',
                    borderRadius: '8px',
                    lineHeight: '1.8',
                    color: '#444',
                    whiteSpace: 'pre-line'
                  }}>
                    {book.fullContent}
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* 구매 확인 모달 */}
      {showPurchaseModal && (
        <div style={{
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: 'rgba(0,0,0,0.5)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          zIndex: 1000
        }}>
          <div style={{
            backgroundColor: '#fff',
            borderRadius: '8px',
            padding: '2rem',
            maxWidth: '400px',
            width: '90%'
          }}>
            <h3 style={{ marginBottom: '1rem', color: '#333' }}>도서 구매 확인</h3>
            
            <div style={{
              padding: '1rem',
              backgroundColor: '#f8f9fa',
              borderRadius: '4px',
              marginBottom: '1.5rem'
            }}>
              <h4 style={{ color: '#333', marginBottom: '0.5rem' }}>{book.title}</h4>
              <p style={{ color: '#666', fontSize: '0.9rem', marginBottom: '0.5rem' }}>
                저자: {book.author}
              </p>
              <div style={{
                fontSize: '1.2rem',
                fontWeight: 'bold',
                color: '#007bff'
              }}>
                {book.price.toLocaleString()}P
              </div>
            </div>

            <p style={{ color: '#666', marginBottom: '1.5rem', fontSize: '0.9rem' }}>
              구매 후 바로 읽을 수 있습니다. 구매하시겠습니까?
            </p>

            <div style={{ display: 'flex', gap: '1rem' }}>
              <button
                onClick={() => setShowPurchaseModal(false)}
                style={{
                  flex: 1,
                  padding: '0.8rem',
                  backgroundColor: '#6c757d',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer'
                }}
              >
                취소
              </button>
              <button
                onClick={handlePurchase}
                style={{
                  flex: 1,
                  padding: '0.8rem',
                  backgroundColor: '#007bff',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer',
                  fontWeight: '500'
                }}
              >
                구매하기
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default BookDetailPage;