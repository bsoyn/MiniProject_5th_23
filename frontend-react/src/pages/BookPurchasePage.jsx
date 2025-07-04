import React, { useState, useEffect } from 'react';
import { useNavigate, useParams, useSearchParams } from 'react-router-dom';

const BASE_URL = process.env.REACT_APP_API_BASE_URL;

const BookPurchasePage = () => {
  const navigate = useNavigate();
  const { bookId: paramBookId } = useParams();
  const [searchParams] = useSearchParams();
  const queryBookId = searchParams.get('bookId');
  
  // bookId는 path parameter 또는 query parameter에서 가져오기
  const bookId = paramBookId || queryBookId;
  
  // 상태 관리
  const [book, setBook] = useState(null);
  const [user, setUser] = useState(null);
  const [userPoints, setUserPoints] = useState(0);
  const [loading, setLoading] = useState(true);
  const [purchaseStep, setPurchaseStep] = useState('confirm'); // confirm, payment, complete
  const [paymentMethod, setPaymentMethod] = useState('points'); // points, charge
  const [agreeTerms, setAgreeTerms] = useState(false);
  const [isProcessing, setIsProcessing] = useState(false);

  // 세션 스토리지에서 사용자 정보 읽어오기 및 토큰으로 사용자 정보 가져오기
  useEffect(() => {
    const loadUserInfo = async () => {
      const userInfo = sessionStorage.getItem('userInfo');
      const accessToken = sessionStorage.getItem('accessToken');
      
      console.log('세션 스토리지 확인:', { userInfo, accessToken }); // 디버깅용
      
      if (!accessToken) {
        console.log('토큰이 없습니다. 로그인 페이지로 이동합니다.');
        navigate('/login');
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
          email: '' // API에서 이메일을 제공하지 않는 경우
        });

        // 독자가 아닌 경우 접근 차단
        if (userType !== 'reader') {
          alert('독자만 도서를 구매할 수 있습니다.');
          navigate('/bookListPage');
          return;
        }

      } catch (error) {
        console.error('사용자 정보 로딩 실패:', error);
        alert('사용자 정보를 불러오는 중 오류가 발생했습니다. 다시 로그인해주세요.');
        sessionStorage.removeItem('userInfo');
        sessionStorage.removeItem('accessToken');
        navigate('/login');
      }
    };

    loadUserInfo();
  }, [navigate]);

  // 도서 정보와 포인트 정보 불러오기
  useEffect(() => {
    if (!bookId || !user) {
      return; // user가 아직 로드되지 않았으면 기다림
    }

    console.log('데이터 로딩 시작 - bookId:', bookId, 'user.id:', user.id); // 디버깅용

    const loadData = async () => {
      setLoading(true);
      try {
        // 도서 정보 조회
        const bookResponse = await fetch(`${BASE_URL}/books/${bookId}`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${sessionStorage.getItem('accessToken')}`,
            'Content-Type': 'application/json',
          },
        });

        if (!bookResponse.ok) {
          throw new Error('도서 정보를 불러올 수 없습니다');
        }

        const bookData = await bookResponse.json();
        console.log('도서 정보 로드 성공:', bookData); // 디버깅용
        setBook(bookData);

        // 포인트 정보 조회 - user.id 사용
        const pointsResponse = await fetch(`${BASE_URL}/points/reader/${user.id}`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${sessionStorage.getItem('accessToken')}`,
            'Content-Type': 'application/json',
          },
        });

        if (!pointsResponse.ok) {
          throw new Error('포인트 정보를 불러올 수 없습니다');
        }

        const pointsData = await pointsResponse.json();
        console.log('포인트 정보 로드 성공:', pointsData); // 디버깅용
        setUserPoints(pointsData.totalPoint);

      } catch (error) {
        console.error('데이터 로딩 실패:', error);
        alert('정보를 불러오는 중 오류가 발생했습니다.');
        navigate('/bookListPage');
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [bookId, user, navigate]); // user를 dependency에 추가

  // 포인트가 충분한지 확인
  const hasEnoughPoints = userPoints >= (book?.price || 0);

  // 포인트로 구매
  const handlePointsPurchase = async () => {
    if (!hasEnoughPoints) {
      alert('포인트가 부족합니다.');
      return;
    }

    if (!agreeTerms) {
      alert('구매 약관에 동의해주세요.');
      return;
    }

    setIsProcessing(true);
    
    try {
      const accessToken = sessionStorage.getItem('accessToken');
      console.log('구매 API 호출 시작');
      console.log('- BASE_URL:', BASE_URL);
      console.log('- accessToken 존재 여부:', !!accessToken);
      console.log('- accessToken 일부:', accessToken ? accessToken.substring(0, 20) + '...' : 'null');
      console.log('- readerId:', user.id);
      console.log('- bookId:', bookId, '(type:', typeof bookId, ')');
      console.log('- price:', book.price);

      const requestBody = {
        readerId: user.id,
        bookId: parseInt(bookId),
        price: book.price
      };
      console.log('- 요청 body:', requestBody);

      // 실제 구매 API 호출
      const purchaseResponse = await fetch(`${BASE_URL}/purchasedBooks/purchasebook`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
      });

      console.log('API 응답 상태:', purchaseResponse.status, purchaseResponse.statusText);
      console.log('응답 헤더 Content-Type:', purchaseResponse.headers.get('Content-Type'));

      // 응답이 JSON인지 확인
      const contentType = purchaseResponse.headers.get('Content-Type');
      const isJsonResponse = contentType && contentType.includes('application/json');

      if (!purchaseResponse.ok) {
        if (isJsonResponse) {
          const errorData = await purchaseResponse.json();
          console.error('API 에러 응답:', errorData);
          throw new Error(errorData.message || `HTTP ${purchaseResponse.status}: ${purchaseResponse.statusText}`);
        } else {
          // JSON이 아닌 응답 (HTML 등)
          const errorText = await purchaseResponse.text();
          console.error('비JSON 에러 응답:', errorText.substring(0, 200) + '...');
          
          if (purchaseResponse.status === 401) {
            throw new Error('인증이 만료되었습니다. 다시 로그인해주세요.');
          } else if (purchaseResponse.status === 404) {
            throw new Error('API 엔드포인트를 찾을 수 없습니다. 서버 설정을 확인해주세요.');
          } else {
            throw new Error(`서버 오류 (${purchaseResponse.status}): ${purchaseResponse.statusText}`);
          }
        }
      }

      // 성공 응답 처리
      if (isJsonResponse) {
        const purchaseResult = await purchaseResponse.json();
        console.log('구매 API 성공 응답:', purchaseResult);
      } else {
        console.log('구매 성공 (비JSON 응답)');
      }
      
      // 구매 성공 시 포인트 차감 (UI 업데이트용)
      setUserPoints(prev => prev - book.price);
      
      // 구매 완료 단계로 이동
      setPurchaseStep('complete');
      
    } catch (error) {
      console.error('구매 실패:', error);
      if (error.message.includes('인증이 만료')) {
        // 토큰이 만료된 경우 로그인 페이지로 이동
        sessionStorage.removeItem('userInfo');
        sessionStorage.removeItem('accessToken');
        alert('로그인이 만료되었습니다. 다시 로그인해주세요.');
        navigate('/login');
      } else {
        alert(`구매 중 오류가 발생했습니다: ${error.message}`);
      }
    } finally {
      setIsProcessing(false);
    }
  };

  // 로그아웃 처리
  const handleLogout = () => {
    sessionStorage.removeItem('userInfo');
    sessionStorage.removeItem('accessToken');
    navigate('/');
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
          <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>💳</div>
          <p>구매 정보를 불러오는 중...</p>
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
            {user && (
              <>
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
                  <span style={{ fontSize: '0.8rem' }}>
                    (보유: {userPoints.toLocaleString()}P)
                  </span>
                </div>
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
              </>
            )}
          </nav>
        </div>
      </header>

      <div style={{ maxWidth: '800px', margin: '0 auto', padding: '2rem' }}>
        {/* 진행 단계 표시 */}
        <div style={{
          display: 'flex',
          justifyContent: 'center',
          marginBottom: '3rem'
        }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
            <div style={{
              width: '30px',
              height: '30px',
              borderRadius: '50%',
              backgroundColor: '#007bff',
              color: '#fff',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              fontSize: '0.9rem',
              fontWeight: 'bold'
            }}>
              1
            </div>
            <span style={{ color: '#007bff', fontWeight: '500' }}>구매 확인</span>
            
            <div style={{ width: '30px', height: '2px', backgroundColor: '#ddd' }} />
            
            <div style={{
              width: '30px',
              height: '30px',
              borderRadius: '50%',
              backgroundColor: purchaseStep === 'payment' || purchaseStep === 'complete' ? '#007bff' : '#ddd',
              color: '#fff',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              fontSize: '0.9rem',
              fontWeight: 'bold'
            }}>
              2
            </div>
            <span style={{ 
              color: purchaseStep === 'payment' || purchaseStep === 'complete' ? '#007bff' : '#666',
              fontWeight: '500' 
            }}>
              결제
            </span>
            
            <div style={{ width: '30px', height: '2px', backgroundColor: '#ddd' }} />
            
            <div style={{
              width: '30px',
              height: '30px',
              borderRadius: '50%',
              backgroundColor: purchaseStep === 'complete' ? '#28a745' : '#ddd',
              color: '#fff',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              fontSize: '0.9rem',
              fontWeight: 'bold'
            }}>
              3
            </div>
            <span style={{ 
              color: purchaseStep === 'complete' ? '#28a745' : '#666',
              fontWeight: '500' 
            }}>
              완료
            </span>
          </div>
        </div>

        {/* 1단계: 구매 확인 */}
        {purchaseStep === 'confirm' && (
          <div style={{
            backgroundColor: '#fff',
            borderRadius: '8px',
            padding: '2rem',
            boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
          }}>
            <h2 style={{
              fontSize: '1.8rem',
              fontWeight: 'bold',
              color: '#333',
              marginBottom: '2rem',
              textAlign: 'center'
            }}>
              구매 확인
            </h2>

            {/* 도서 정보 */}
            <div style={{
              display: 'flex',
              gap: '2rem',
              padding: '1.5rem',
              backgroundColor: '#f8f9fa',
              borderRadius: '8px',
              marginBottom: '2rem'
            }}>
              <img
                src={book.imageUrl}
                alt={book.title}
                style={{
                  width: '120px',
                  height: '168px',
                  borderRadius: '4px',
                  objectFit: 'cover'
                }}
              />
              <div style={{ flex: 1 }}>
                <h3 style={{
                  fontSize: '1.3rem',
                  fontWeight: 'bold',
                  color: '#333',
                  marginBottom: '0.5rem'
                }}>
                  {book.title}
                </h3>
                <p style={{ color: '#666', marginBottom: '0.5rem' }}>
                  저자: {book.authorName}
                </p>
                <p style={{ color: '#666', marginBottom: '0.5rem' }}>
                  카테고리: {book.category}
                </p>
                <p style={{ color: '#666', marginBottom: '1rem', lineHeight: '1.4' }}>
                  {book.summary}
                </p>
                <div style={{
                  fontSize: '1.5rem',
                  fontWeight: 'bold',
                  color: '#007bff'
                }}>
                  {book.price.toLocaleString()}P
                </div>
              </div>
            </div>

            {/* 현재 포인트 정보 */}
            <div style={{
              padding: '1.5rem',
              backgroundColor: hasEnoughPoints ? '#e8f5e8' : '#fff3cd',
              border: hasEnoughPoints ? '1px solid #d4edda' : '1px solid #ffeaa7',
              borderRadius: '8px',
              marginBottom: '2rem'
            }}>
              <h4 style={{ color: '#333', marginBottom: '1rem' }}>포인트 현황</h4>
              <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                marginBottom: '0.5rem'
              }}>
                <span>현재 보유 포인트</span>
                <span style={{ fontWeight: 'bold' }}>{userPoints.toLocaleString()}P</span>
              </div>
              <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                marginBottom: '0.5rem'
              }}>
                <span>도서 가격</span>
                <span style={{ fontWeight: 'bold' }}>-{book.price.toLocaleString()}P</span>
              </div>
              <hr style={{ margin: '1rem 0', border: 'none', borderTop: '1px solid #ddd' }} />
              <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                fontSize: '1.1rem',
                fontWeight: 'bold'
              }}>
                <span>구매 후 잔액</span>
                <span style={{ 
                  color: hasEnoughPoints ? '#28a745' : '#dc3545' 
                }}>
                  {hasEnoughPoints 
                    ? `${(userPoints - book.price).toLocaleString()}P` 
                    : `${(userPoints - book.price).toLocaleString()}P (부족)`
                  }
                </span>
              </div>
            </div>

            {/* 결제 방법 선택 */}
            <div style={{ marginBottom: '2rem' }}>
              <h4 style={{ color: '#333', marginBottom: '1rem' }}>결제 방법</h4>
              <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                <label style={{
                  display: 'flex',
                  alignItems: 'center',
                  padding: '1rem',
                  border: paymentMethod === 'points' && hasEnoughPoints ? '2px solid #007bff' : '1px solid #ddd',
                  borderRadius: '8px',
                  cursor: hasEnoughPoints ? 'pointer' : 'not-allowed',
                  backgroundColor: paymentMethod === 'points' && hasEnoughPoints ? '#f0f8ff' : '#fff',
                  opacity: hasEnoughPoints ? 1 : 0.6
                }}>
                  <input
                    type="radio"
                    name="paymentMethod"
                    value="points"
                    checked={paymentMethod === 'points'}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                    disabled={!hasEnoughPoints}
                    style={{ marginRight: '1rem' }}
                  />
                  <div>
                    <div style={{ fontWeight: '500', color: '#333' }}>
                      보유 포인트로 결제
                    </div>
                    <div style={{ fontSize: '0.9rem', color: '#666' }}>
                      현재 {userPoints.toLocaleString()}P 보유 중
                      {!hasEnoughPoints && ` (${(book.price - userPoints).toLocaleString()}P 부족)`}
                    </div>
                  </div>
                </label>

                <label style={{
                  display: 'flex',
                  alignItems: 'center',
                  padding: '1rem',
                  border: paymentMethod === 'charge' ? '2px solid #007bff' : '1px solid #ddd',
                  borderRadius: '8px',
                  cursor: 'pointer',
                  backgroundColor: paymentMethod === 'charge' ? '#f0f8ff' : '#fff'
                }}>
                  <input
                    type="radio"
                    name="paymentMethod"
                    value="charge"
                    checked={paymentMethod === 'charge'}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                    style={{ marginRight: '1rem' }}
                  />
                  <div>
                    <div style={{ fontWeight: '500', color: '#333' }}>
                      포인트 충전 후 결제
                    </div>
                    <div style={{ fontSize: '0.9rem', color: '#666' }}>
                      마이페이지에서 포인트를 충전하고 구매
                    </div>
                  </div>
                </label>
              </div>
            </div>

            {/* 이용약관 동의 */}
            <div style={{
              padding: '1rem',
              backgroundColor: '#f8f9fa',
              borderRadius: '8px',
              marginBottom: '2rem'
            }}>
              <label style={{
                display: 'flex',
                alignItems: 'flex-start',
                gap: '0.5rem',
                cursor: 'pointer'
              }}>
                <input
                  type="checkbox"
                  checked={agreeTerms}
                  onChange={(e) => setAgreeTerms(e.target.checked)}
                  style={{ marginTop: '0.2rem' }}
                />
                <div>
                  <span style={{ fontWeight: '500' }}>구매 약관에 동의합니다</span>
                  <div style={{ fontSize: '0.8rem', color: '#666', marginTop: '0.3rem' }}>
                    • 디지털 콘텐츠 특성상 구매 후 환불이 불가합니다<br />
                    • 구매한 도서는 계정에서 영구적으로 이용 가능합니다<br />
                    • 저작권법에 따라 무단 복제 및 배포는 금지됩니다
                  </div>
                </div>
              </label>
            </div>

            {/* 버튼 */}
            <div style={{ display: 'flex', gap: '1rem' }}>
              <button
                onClick={() => navigate('/bookListPage')}
                style={{
                  flex: 1,
                  padding: '1rem',
                  backgroundColor: '#6c757d',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '8px',
                  cursor: 'pointer',
                  fontSize: '1rem'
                }}
              >
                취소
              </button>
              {paymentMethod === 'charge' ? (
                <button
                  onClick={() => navigate('/readerMypage')}
                  style={{
                    flex: 2,
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
                  포인트 충전하러 가기
                </button>
              ) : (
                <button
                  onClick={() => setPurchaseStep('payment')}
                  disabled={!agreeTerms || !hasEnoughPoints}
                  style={{
                    flex: 2,
                    padding: '1rem',
                    backgroundColor: (agreeTerms && hasEnoughPoints) ? '#007bff' : '#ddd',
                    color: '#fff',
                    border: 'none',
                    borderRadius: '8px',
                    cursor: (agreeTerms && hasEnoughPoints) ? 'pointer' : 'not-allowed',
                    fontSize: '1rem',
                    fontWeight: 'bold'
                  }}
                >
                  다음 단계
                </button>
              )}
            </div>
          </div>
        )}

        {/* 2단계: 결제 */}
        {purchaseStep === 'payment' && (
          <div style={{
            backgroundColor: '#fff',
            borderRadius: '8px',
            padding: '2rem',
            boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
          }}>
            <h2 style={{
              fontSize: '1.8rem',
              fontWeight: 'bold',
              color: '#333',
              marginBottom: '2rem',
              textAlign: 'center'
            }}>
              결제
            </h2>

            <div>
              <h3 style={{ color: '#333', marginBottom: '1.5rem' }}>포인트 결제</h3>
              
              <div style={{
                padding: '1.5rem',
                backgroundColor: '#f8f9fa',
                borderRadius: '8px',
                marginBottom: '2rem'
              }}>
                <div style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  marginBottom: '0.5rem'
                }}>
                  <span>도서 가격</span>
                  <span style={{ fontWeight: 'bold' }}>{book.price.toLocaleString()}P</span>
                </div>
                <div style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  marginBottom: '0.5rem'
                }}>
                  <span>보유 포인트</span>
                  <span>{userPoints.toLocaleString()}P</span>
                </div>
                <hr style={{ margin: '1rem 0' }} />
                <div style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  fontSize: '1.1rem',
                  fontWeight: 'bold'
                }}>
                  <span>결제 후 잔액</span>
                  <span style={{ color: '#28a745' }}>
                    {(userPoints - book.price).toLocaleString()}P
                  </span>
                </div>
              </div>

              <div style={{ display: 'flex', gap: '1rem' }}>
                <button
                  onClick={() => setPurchaseStep('confirm')}
                  style={{
                    flex: 1,
                    padding: '1rem',
                    backgroundColor: '#6c757d',
                    color: '#fff',
                    border: 'none',
                    borderRadius: '8px',
                    cursor: 'pointer',
                    fontSize: '1rem'
                  }}
                >
                  이전 단계
                </button>
                <button
                  onClick={handlePointsPurchase}
                  disabled={isProcessing}
                  style={{
                    flex: 2,
                    padding: '1rem',
                    backgroundColor: isProcessing ? '#ddd' : '#007bff',
                    color: '#fff',
                    border: 'none',
                    borderRadius: '8px',
                    cursor: isProcessing ? 'not-allowed' : 'pointer',
                    fontSize: '1rem',
                    fontWeight: 'bold'
                  }}
                >
                  {isProcessing ? '결제 처리 중...' : '결제하기'}
                </button>
              </div>
            </div>
          </div>
        )}

        {/* 3단계: 구매 완료 */}
        {purchaseStep === 'complete' && (
          <div style={{
            backgroundColor: '#fff',
            borderRadius: '8px',
            padding: '3rem 2rem',
            boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
            textAlign: 'center'
          }}>
            <div style={{
              fontSize: '4rem',
              marginBottom: '1.5rem'
            }}>
              🎉
            </div>
            
            <h2 style={{
              fontSize: '2rem',
              fontWeight: 'bold',
              color: '#28a745',
              marginBottom: '1rem'
            }}>
              구매가 완료되었습니다!
            </h2>
            
            <p style={{
              fontSize: '1.1rem',
              color: '#666',
              marginBottom: '2rem'
            }}>
              "{book.title}"을(를) 성공적으로 구매하였습니다.
            </p>

            {/* 구매 정보 요약 */}
            <div style={{
              padding: '1.5rem',
              backgroundColor: '#f8f9fa',
              borderRadius: '8px',
              marginBottom: '2rem',
              textAlign: 'left'
            }}>
              <h4 style={{ color: '#333', marginBottom: '1rem', textAlign: 'center' }}>구매 내역</h4>
              <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                marginBottom: '0.5rem'
              }}>
                <span>도서명</span>
                <span style={{ fontWeight: 'bold' }}>{book.title}</span>
              </div>
              <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                marginBottom: '0.5rem'
              }}>
                <span>저자</span>
                <span>{book.authorName}</span>
              </div>
              <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                marginBottom: '0.5rem'
              }}>
                <span>결제 금액</span>
                <span style={{ fontWeight: 'bold' }}>{book.price.toLocaleString()}P</span>
              </div>
              <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                marginBottom: '0.5rem'
              }}>
                <span>구매 일시</span>
                <span>{new Date().toLocaleString()}</span>
              </div>
              <hr style={{ margin: '1rem 0' }} />
              <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                fontSize: '1.1rem',
                fontWeight: 'bold'
              }}>
                <span>현재 잔액</span>
                <span style={{ color: '#007bff' }}>{userPoints.toLocaleString()}P</span>
              </div>
            </div>

            <div style={{
              display: 'flex',
              flexDirection: 'column',
              gap: '1rem',
              maxWidth: '400px',
              margin: '0 auto'
            }}>
              <button
                onClick={() => navigate(`/book-detail/${bookId}`)}
                style={{
                  padding: '1rem 2rem',
                  backgroundColor: '#28a745',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '8px',
                  cursor: 'pointer',
                  fontSize: '1.1rem',
                  fontWeight: 'bold'
                }}
              >
                📖 지금 바로 읽기
              </button>
              
              <button
                onClick={() => navigate('/readerMypage')}
                style={{
                  padding: '1rem 2rem',
                  backgroundColor: '#007bff',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '8px',
                  cursor: 'pointer',
                  fontSize: '1rem'
                }}
              >
                내 서재로 이동
              </button>
              
              <button
                onClick={() => navigate('/bookListPage')}
                style={{
                  padding: '1rem 2rem',
                  backgroundColor: 'transparent',
                  color: '#666',
                  border: '1px solid #ddd',
                  borderRadius: '8px',
                  cursor: 'pointer',
                  fontSize: '1rem'
                }}
              >
                다른 도서 둘러보기
              </button>
            </div>

            {/* 구매 혜택 안내 */}
            <div style={{
              marginTop: '2rem',
              padding: '1rem',
              backgroundColor: '#e8f5e8',
              border: '1px solid #d4edda',
              borderRadius: '8px'
            }}>
              <h4 style={{ color: '#155724', marginBottom: '0.5rem' }}>구매 혜택</h4>
              <ul style={{
                textAlign: 'left',
                color: '#155724',
                fontSize: '0.9rem',
                margin: 0,
                paddingLeft: '1.2rem'
              }}>
                <li>구매한 도서는 영구적으로 이용 가능합니다</li>
                <li>모든 기기에서 동기화되어 읽을 수 있습니다</li>
                <li>북마크, 메모 기능을 자유롭게 사용하세요</li>
                <li>리뷰 작성으로 다른 독자들과 소통해보세요</li>
              </ul>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default BookPurchasePage;