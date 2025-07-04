import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

const BASE_URL = process.env.REACT_APP_API_BASE_URL;

const BookDetailPage = () => {
  const navigate = useNavigate();
  const { bookId } = useParams();
  
  // 상태 관리
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);
  const [user, setUser] = useState(null);
  const [isPurchased, setIsPurchased] = useState(false);
  const [hasSubscription, setHasSubscription] = useState(false);
  const [subscriptionInfo, setSubscriptionInfo] = useState(null);
  const [accessGranted, setAccessGranted] = useState(false); // 구매 또는 구독권으로 접근 가능한지
  const [showPurchaseModal, setShowPurchaseModal] = useState(false);
  const [imageError, setImageError] = useState(false); // 이미지 로딩 에러 상태
  const [showFullContent, setShowFullContent] = useState(false); // 전체 내용 표시 여부

  // 토큰으로 사용자 정보 가져오기
  useEffect(() => {
    const loadUserInfo = async () => {
      const userInfo = sessionStorage.getItem('userInfo');
      const accessToken = sessionStorage.getItem('accessToken');
      
      console.log('사용자 정보 로딩 시작');
      
      if (!accessToken) {
        console.log('토큰이 없음 - 비로그인 상태');
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
        console.log('API에서 받은 사용자 정보:', userData);

        // 세션 스토리지의 userType과 API 응답 조합
        let userType = 'reader';
        if (userInfo) {
          try {
            const parsedUser = JSON.parse(userInfo);
            userType = parsedUser.userType?.toLowerCase() || 'reader';
          } catch (error) {
            console.warn('세션 스토리지 파싱 실패, 기본값 사용');
          }
        }

        setUser({
          id: userData.userId,
          name: userData.userName,
          type: userType,
          email: ''
        });

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

  // 도서 정보 불러오기
  useEffect(() => {
    if (!bookId) return;

    const loadBook = async () => {
      setLoading(true);
      try {
        console.log('도서 정보 로딩 시작 - bookId:', bookId);

        // 토큰이 있는 경우와 없는 경우 모두 처리
        const accessToken = sessionStorage.getItem('accessToken');
        const headers = {
          'Content-Type': 'application/json',
        };
        
        if (accessToken) {
          headers['Authorization'] = `Bearer ${accessToken}`;
        }

        // 도서 정보 조회
        const response = await fetch(`${BASE_URL}/books/${bookId}`, {
          method: 'GET',
          headers: headers,
        });

        if (!response.ok) {
          if (response.status === 403 || response.status === 401) {
            console.warn('인증 없이 도서 정보 조회 시도');
            // 토큰 없이 다시 시도
            const retryResponse = await fetch(`${BASE_URL}/books/${bookId}`, {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json',
              },
            });
            
            if (!retryResponse.ok) {
              throw new Error('도서 정보를 불러올 수 없습니다');
            }
            
            const bookData = await retryResponse.json();
            console.log('도서 정보 로드 성공 (인증 없음):', bookData);
            setBook(bookData);
            setImageError(false); // 이미지 에러 상태 초기화
          } else {
            throw new Error('도서 정보를 불러올 수 없습니다');
          }
        } else {
          const bookData = await response.json();
          console.log('도서 정보 로드 성공:', bookData);
          setBook(bookData);
          setImageError(false); // 이미지 에러 상태 초기화
        }

      } catch (error) {
        console.error('도서 정보 불러오기 실패:', error);
        alert('도서 정보를 불러오는 중 오류가 발생했습니다.');
      } finally {
        setLoading(false);
      }
    };

    loadBook();
  }, [bookId]);

  // 구매 여부 및 구독권 확인
  useEffect(() => {
    if (!user || !bookId) return;

    const checkAccessRights = async () => {
      try {
        console.log('접근 권한 확인 시작 - userId:', user.id, 'bookId:', bookId);

        // 1. 구매 여부 확인 (임시로 비활성화)
        try {
          const purchaseResponse = await fetch(`${BASE_URL}/purchasedBooks/check/${user.id}/${bookId}`, {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${sessionStorage.getItem('accessToken')}`,
              'Content-Type': 'application/json',
            },
          });

          if (purchaseResponse.ok) {
            const purchaseData = await purchaseResponse.json();
            setIsPurchased(purchaseData.isPurchased);
            console.log('구매 여부:', purchaseData.isPurchased);
          } else {
            console.warn('구매 여부 확인 API 오류, 기본값 사용');
            setIsPurchased(false);
          }
        } catch (purchaseError) {
          console.warn('구매 여부 확인 실패 (API 미구현?), 기본값 사용:', purchaseError);
          setIsPurchased(false);
        }

        // 2. 구독권 여부 확인 (500 에러 처리 포함)
        try {
          const subscriptionResponse = await fetch(`${BASE_URL}/subscribes/reader/${user.id}`, {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${sessionStorage.getItem('accessToken')}`,
              'Content-Type': 'application/json',
            },
          });

          if (subscriptionResponse.ok) {
            const subscriptionData = await subscriptionResponse.json();
            console.log('구독권 정보:', subscriptionData);
            
            setSubscriptionInfo(subscriptionData);
            setHasSubscription(subscriptionData.isSubscribed || false);
          } else {
            console.warn('구독권 정보 없음 (구독하지 않음)');
            setHasSubscription(false);
            setSubscriptionInfo(null);
          }
        } catch (subscriptionError) {
          console.warn('구독권 확인 중 오류 (구독하지 않음):', subscriptionError);
          setHasSubscription(false);
          setSubscriptionInfo(null);
        }

      } catch (error) {
        console.error('접근 권한 확인 실패:', error);
        setIsPurchased(false);
        setHasSubscription(false);
      }
    };

    checkAccessRights();
  }, [user, bookId]);

  // 접근 권한 계산
  useEffect(() => {
    const canAccess = isPurchased || hasSubscription;
    setAccessGranted(canAccess);
    console.log('접근 권한:', { isPurchased, hasSubscription, canAccess });
  }, [isPurchased, hasSubscription]);

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

    // 구매 페이지로 이동
    navigate(`/bookPurchase/${bookId}`);
  };

  // 구독권 구매
  const handleSubscription = () => {
    if (!user) {
      alert('로그인이 필요합니다.');
      navigate('/login');
      return;
    }

    if (user.type !== 'reader') {
      alert('독자만 구독할 수 있습니다.');
      return;
    }

    // 독자 마이페이지로 이동 (구독권 구매)
    navigate('/readerMypage');
  };

  // 도서 읽기
  const handleReadBook = () => {
    if (!accessGranted) {
      alert('구매하거나 구독권이 있어야 읽을 수 있습니다.');
      return;
    }
    
    // 전체 내용 표시
    setShowFullContent(true);
    
    // 전체 내용 섹션으로 스크롤 이동
    setTimeout(() => {
      const contentElement = document.getElementById('full-content-section');
      if (contentElement) {
        contentElement.scrollIntoView({ 
          behavior: 'smooth',
          block: 'start'
        });
      }
    }, 100);
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
            onClick={() => navigate('/bookListPage')}
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
          
          <nav style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
            <button
              onClick={() => navigate('/bookListPage')}
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

            {user ? (
              <div style={{
                display: 'flex',
                alignItems: 'center',
                gap: '0.5rem',
                padding: '0.5rem 1rem',
                backgroundColor: '#007bff',
                color: '#fff',
                borderRadius: '20px',
                fontSize: '0.9rem'
              }}>
                <span style={{ fontSize: '1.2rem' }}>👤</span>
                <span>{user.name}</span>
                <span style={{ fontSize: '0.8rem' }}>({user.type})</span>
              </div>
            ) : (
              <button
                onClick={() => navigate('/login')}
                style={{
                  padding: '0.5rem 1rem',
                  backgroundColor: '#007bff',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer',
                  fontSize: '0.9rem'
                }}
              >
                로그인
              </button>
            )}
          </nav>
        </div>
      </header>

      <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '2rem' }}>
        {/* 뒤로가기 버튼 */}
        <button
          onClick={() => navigate('/bookListPage')}
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
              {!imageError ? (
                <img
                  src={book.imageUrl}
                  alt={book.title}
                  onError={() => setImageError(true)}
                  style={{
                    width: '100%',
                    height: '400px',
                    objectFit: 'cover',
                    backgroundColor: '#f0f0f0',
                    borderRadius: '8px',
                    marginBottom: '1.5rem'
                  }}
                />
              ) : (
                <div style={{
                  width: '100%',
                  height: '400px',
                  backgroundColor: '#f0f0f0',
                  borderRadius: '8px',
                  marginBottom: '1.5rem',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  flexDirection: 'column',
                  color: '#666'
                }}>
                  <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>📚</div>
                  <div style={{ fontSize: '1rem' }}>{book.title}</div>
                  <div style={{ fontSize: '0.8rem', marginTop: '0.5rem' }}>이미지를 불러올 수 없습니다</div>
                </div>
              )}

              {/* 접근 상태 표시 */}
              {user && (
                <div style={{
                  padding: '1rem',
                  backgroundColor: accessGranted ? '#e8f5e8' : '#fff3cd',
                  border: accessGranted ? '1px solid #d4edda' : '1px solid #ffeaa7',
                  borderRadius: '8px',
                  marginBottom: '1.5rem',
                  textAlign: 'center'
                }}>
                  {isPurchased && (
                    <div style={{ color: '#155724', fontWeight: '500', marginBottom: '0.5rem' }}>
                      ✅ 구매 완료
                    </div>
                  )}
                  {hasSubscription && subscriptionInfo && (
                    <div style={{ color: '#155724', fontWeight: '500', marginBottom: '0.5rem' }}>
                      📅 구독권 보유 (만료: {new Date(subscriptionInfo.subscribeEndDate).toLocaleDateString()})
                    </div>
                  )}
                  {!accessGranted && (
                    <div style={{ color: '#856404', fontSize: '0.9rem' }}>
                      읽으려면 구매하거나 구독권이 필요합니다
                    </div>
                  )}
                </div>
              )}

              {/* 구매/읽기 버튼 */}
              <div style={{ marginBottom: '1.5rem' }}>
                {accessGranted ? (
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
                  <>
                    <button
                      onClick={handlePurchase}
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
                    
                    <div style={{
                      textAlign: 'center',
                      margin: '1rem 0',
                      color: '#666',
                      fontSize: '0.9rem'
                    }}>
                      또는
                    </div>
                    
                    <button
                      onClick={handleSubscription}
                      style={{
                        width: '100%',
                        padding: '1rem',
                        backgroundColor: '#28a745',
                        color: '#fff',
                        border: 'none',
                        borderRadius: '8px',
                        cursor: 'pointer',
                        fontSize: '1rem',
                        fontWeight: 'bold'
                      }}
                    >
                      📅 구독권으로 읽기
                    </button>
                  </>
                )}
                
                <div style={{
                  textAlign: 'center',
                  fontSize: '0.8rem',
                  color: '#666',
                  marginTop: '0.5rem'
                }}>
                  {accessGranted ? '지금 바로 읽을 수 있습니다' : '구매 또는 구독권이 필요합니다'}
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
                    <span style={{ color: '#666' }}>저자</span>
                    <span style={{ fontWeight: '500' }}>{book.authorName}</span>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span style={{ color: '#666' }}>조회수</span>
                    <span style={{ fontWeight: '500' }}>{book.views.toLocaleString()}</span>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span style={{ color: '#666' }}>가격</span>
                    <span style={{ fontWeight: '500' }}>{book.price.toLocaleString()}P</span>
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
                by {book.authorName}
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
                    {book.price.toLocaleString()}P
                  </div>
                  <div style={{ fontSize: '0.8rem', color: '#666' }}>가격</div>
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
                {book.summary}
              </p>

              {/* 전체 내용 (접근 권한이 있을 때만 표시) */}
              {accessGranted ? (
                <div id="full-content-section">
                  <h3 style={{ color: '#333', marginBottom: '1rem' }}>
                    {showFullContent ? '📖 도서 전체 내용' : '작품 미리보기'}
                  </h3>
                  
                  {showFullContent ? (
                    // 전체 내용 표시
                    <div style={{
                      padding: '2rem',
                      backgroundColor: '#fff',
                      borderRadius: '8px',
                      lineHeight: '2',
                      color: '#333',
                      fontSize: '1.1rem',
                      border: '1px solid #e5e5e5',
                      maxHeight: '80vh',
                      overflowY: 'auto'
                    }}>
                      <div style={{
                        marginBottom: '2rem',
                        padding: '1rem',
                        backgroundColor: '#f8f9fa',
                        borderRadius: '4px',
                        borderLeft: '4px solid #007bff'
                      }}>
                        <h4 style={{ margin: 0, color: '#007bff' }}>{book.title}</h4>
                        <p style={{ margin: '0.5rem 0 0 0', color: '#666', fontSize: '0.9rem' }}>
                          저자: {book.authorName} | {book.category}
                        </p>
                      </div>
                      
                      {/* 실제 도서 내용 - API에서 받아온 content 사용 */}
                      <div style={{ whiteSpace: 'pre-line' }}>
                        {book.content || book.summary || '도서 내용을 불러오는 중입니다...'}
                      </div>
                      
                      <div style={{
                        marginTop: '2rem',
                        padding: '1rem',
                        backgroundColor: '#e8f5e8',
                        borderRadius: '4px',
                        textAlign: 'center'
                      }}>
                        <p style={{ margin: 0, color: '#155724', fontSize: '0.9rem' }}>
                          📚 독서를 완료하셨습니다! 이 작품이 마음에 드셨다면 리뷰를 남겨보세요.
                        </p>
                      </div>
                    </div>
                  ) : (
                    // 미리보기만 표시
                    <div style={{
                      padding: '1.5rem',
                      backgroundColor: '#f8f9fa',
                      borderRadius: '8px',
                      lineHeight: '1.8',
                      color: '#444'
                    }}>
                      <p>{book.summary}</p>
                      <div style={{
                        marginTop: '1rem',
                        padding: '1rem',
                        backgroundColor: '#fff',
                        borderRadius: '4px',
                        border: '2px dashed #007bff',
                        textAlign: 'center'
                      }}>
                        <p style={{ margin: 0, color: '#007bff', fontSize: '0.9rem' }}>
                          💡 "읽기 시작" 버튼을 클릭하면 전체 내용을 읽을 수 있습니다!
                        </p>
                      </div>
                    </div>
                  )}
                </div>
              ) : (
                <div style={{
                  padding: '2rem',
                  backgroundColor: '#f8f9fa',
                  borderRadius: '8px',
                  textAlign: 'center',
                  border: '2px dashed #ddd'
                }}>
                  <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>🔒</div>
                  <h3 style={{ color: '#333', marginBottom: '1rem' }}>전체 내용을 읽으려면</h3>
                  <p style={{ color: '#666', marginBottom: '1.5rem' }}>
                    도서를 구매하거나 구독권을 이용해주세요
                  </p>
                  <div style={{ display: 'flex', gap: '1rem', justifyContent: 'center' }}>
                    <button
                      onClick={handlePurchase}
                      style={{
                        padding: '0.8rem 1.5rem',
                        backgroundColor: '#007bff',
                        color: '#fff',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer',
                        fontSize: '0.9rem'
                      }}
                    >
                      도서 구매
                    </button>
                    <button
                      onClick={handleSubscription}
                      style={{
                        padding: '0.8rem 1.5rem',
                        backgroundColor: '#28a745',
                        color: '#fff',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer',
                        fontSize: '0.9rem'
                      }}
                    >
                      구독권 구매
                    </button>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BookDetailPage;