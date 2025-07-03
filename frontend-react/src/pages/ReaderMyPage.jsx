import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const ReaderMyPage = () => {
  const navigate = useNavigate();
  const [userId, setUserId] = useState("");
  const [userInfo, setUserInfo] = useState(null);
  const [pointInfo, setPointInfo] = useState(null);
  const [subscriptionInfo, setSubscriptionInfo] = useState(null);
  const [purchasedBooks, setPurchasedBooks] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true);
      const accessToken = sessionStorage.getItem('accessToken');
      console.log(accessToken);
      if (!accessToken) {
        navigate('/login');
        return;
      }

      const headers = {
        'Authorization': `Bearer ${accessToken}`,
      };

      try {
        const tokenResponse = await fetch("/api/token", {
          method: 'POST',
          headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${accessToken}` },
        });

        if (!tokenResponse.ok) {
          throw new Error('Token validation failed');
        }

        const tokenData = await tokenResponse.json();
        const currentUserId = tokenData.userId;
        setUserId(currentUserId);
        
        const userResponse = await fetch(`/managerReaders/${currentUserId}`, {
          method: 'GET',
          headers: headers,
        });

        //api url 수정
        // const pointResponse = await fetch(`/points/${currentUserId}`, {
        //   method: 'GET',
        //   headers: headers,
        // });
        // const subscriptionResponse = await fetch(`/subscribes/${currentUserId}`,{
        //   method: 'GET',
        //   headers: headers,
        // });
        // const booksResponse = await fetch(`/availiableBookLists/${currentUserId}`,{
        //   method: 'GET',
        //   headers: headers,
        // });

        // console.log(pointResponse);
        // console.log(subscriptionResponse);
        // console.log(booksResponse);

        const userData = await userResponse.json();
        // const pointData = await pointResponse.json();
        // const subscriptionData = await subscriptionResponse.json();
        // const booksData = await booksResponse.json();

        setUserInfo(userData);
        // setPointInfo(pointData);
        // setSubscriptionInfo(subscriptionData);
        // setPurchasedBooks(booksData);

      } catch (err) {
        console.error(err);
        setError(err.message);
        if (err.message === 'Token validation failed') {
          navigate('/login');
        }
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, [navigate]);

  // ... (모달 관련 로직은 일단 그대로 둡니다)
  const [showChargeModal, setShowChargeModal] = useState(false);
  const [showSubscriptionModal, setShowSubscriptionModal] = useState(false);

  if (isLoading) {
    return <div>로딩 중...</div>;
  }

  if (error) {
    return <div>오류: {error}</div>;
  }
  // const [chargeAmount, setChargeAmount] = useState('');
  
    const handlePointCharge = (amount) => {
      setUserInfo(prev => ({
        ...prev,
        points: prev.points + amount
      }));
      setShowChargeModal(false);
      // setChargeAmount('');
      alert(`${amount.toLocaleString()}P가 충전되었습니다!`);
    };
  
    const handleSubscriptionPurchase = () => {
      if (userInfo.points >= 9900) {
        setUserInfo(prev => ({
          ...prev,
          points: prev.points - 9900,
          subscriptionEndDate: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0]
        }));
        setShowSubscriptionModal(false);
        alert('월 구독권이 구매되었습니다!');
      } else {
        alert('포인트가 부족합니다. 포인트를 충전해주세요.');
      }
    };
  
    // const isSubscriptionActive = userInfo.subscriptionEndDate && new Date(userInfo.subscriptionEndDate) > new Date();
  

  const isSubscriptionActive = subscriptionInfo && new Date(subscriptionInfo.endDate) > new Date();

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
                backgroundColor: '#007bff',
                border: 'none',
                borderRadius: '4px',
                color: '#fff',
                cursor: 'pointer',
                fontSize: '0.9rem'
              }}
            >
              홈으로
            </button>
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
          마이페이지
        </h2>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 2fr', gap: '2rem' }}>
          {/* 왼쪽 사이드바 */}
          <div>
            {/* 사용자 정보 카드 */}
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
                  backgroundColor: '#007bff',
                  color: '#fff',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: '2rem',
                  margin: '0 auto 1rem'
                }}>
                  👤
                </div>
                <h3 style={{ color: '#333', marginBottom: '0.5rem' }}>{userInfo?.name}</h3>
                <p style={{ color: '#666', fontSize: '0.9rem' }}>{userInfo?.email}</p>
                <p style={{ color: '#999', fontSize: '0.8rem' }}>
                  가입일: {userInfo?.joinDate ? new Date(userInfo.joinDate).toLocaleDateString() : '-'}
                </p>
              </div>
            </div>

            {/* 포인트 및 구독권 정보 */}
            <div style={{
              backgroundColor: '#fff',
              borderRadius: '8px',
              padding: '1.5rem',
              boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              marginBottom: '1.5rem'
            }}>
              <h4 style={{ color: '#333', marginBottom: '1rem' }}>보유 현황</h4>
              
              <div style={{ marginBottom: '1rem' }}>
                <div style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginBottom: '0.5rem'
                }}>
                  <span style={{ color: '#666' }}>포인트</span>
                  <span style={{ fontSize: '1.2rem', fontWeight: 'bold', color: '#007bff' }}>
                    {pointInfo?.points.toLocaleString()}P
                  </span>
                </div>
                <button
                  onClick={() => setShowChargeModal(true)}
                  style={{
                    width: '100%',
                    padding: '0.5rem',
                    backgroundColor: '#007bff',
                    color: '#fff',
                    border: 'none',
                    borderRadius: '4px',
                    cursor: 'pointer',
                    fontSize: '0.9rem'
                  }}
                >
                  포인트 충전
                </button>
              </div>

              <div>
                <div style={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  marginBottom: '0.5rem'
                }}>
                  <span style={{ color: '#666' }}>구독권</span>
                  <span style={{
                    fontSize: '0.9rem',
                    color: isSubscriptionActive ? '#28a745' : '#dc3545'
                  }}>
                    {isSubscriptionActive ? `${new Date(subscriptionInfo.endDate).toLocaleDateString()}까지` : '미구독'}
                  </span>
                </div>
                <button
                  onClick={() => setShowSubscriptionModal(true)}
                  disabled={isSubscriptionActive}
                  style={{
                    width: '100%',
                    padding: '0.5rem',
                    backgroundColor: isSubscriptionActive ? '#6c757d' : '#28a745',
                    color: '#fff',
                    border: 'none',
                    borderRadius: '4px',
                    cursor: isSubscriptionActive ? 'not-allowed' : 'pointer',
                    fontSize: '0.9rem'
                  }}
                >
                  {isSubscriptionActive ? '구독 중' : '월 구독권 구매 (9,900P)'}
                </button>
              </div>
            </div>
          </div>

          {/* 오른쪽 메인 컨텐츠 */}
          <div>
            {/* 구매한 도서 목록 */}
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
                구매한 도서 ({purchasedBooks.length}권)
              </h3>

              {purchasedBooks.length > 0 ? (
                <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                  {purchasedBooks.map(book => (
                    <div key={book.id} style={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center',
                      padding: '1rem',
                      border: '1px solid #e5e5e5',
                      borderRadius: '8px',
                      backgroundColor: '#fafafa'
                    }}>
                      <div>
                        <h4 style={{
                          color: '#333',
                          marginBottom: '0.3rem',
                          fontSize: '1.1rem'
                        }}>
                          {book.title}
                        </h4>
                        <p style={{ color: '#666', marginBottom: '0.3rem' }}>
                          저자: {book.authorName}
                        </p>
                        <p style={{ color: '#999', fontSize: '0.8rem' }}>
                          구매일: {new Date(book.purchasedDate).toLocaleDateString()}
                        </p>
                      </div>
                      <div style={{ textAlign: 'right' }}>
                        <div style={{
                          color: '#666',
                          fontSize: '0.9rem',
                          marginBottom: '0.5rem'
                        }}>
                          {book.price.toLocaleString()}P
                        </div>
                        <button style={{
                          padding: '0.4rem 0.8rem',
                          backgroundColor: '#28a745',
                          color: '#fff',
                          border: 'none',
                          borderRadius: '4px',
                          cursor: 'pointer',
                          fontSize: '0.8rem'
                        }}>
                          읽기
                        </button>
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
                  <p>아직 구매한 도서가 없습니다.</p>
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
                    도서 둘러보기
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* 포인트 충전 모달 */}
      {showChargeModal && (
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
            <h3 style={{ marginBottom: '1.5rem', color: '#333' }}>포인트 충전</h3>
            
            <div style={{ marginBottom: '1.5rem' }}>
              <label style={{
                display: 'block',
                marginBottom: '0.5rem',
                color: '#333',
                fontWeight: '500'
              }}>
                충전할 금액을 선택하세요
              </label>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.5rem' }}>
                {[5000, 10000, 20000, 50000].map(amount => (
                  <button
                    key={amount}
                    onClick={() => handlePointCharge(amount)}
                    style={{
                      padding: '0.8rem',
                      border: '1px solid #ddd',
                      borderRadius: '4px',
                      backgroundColor: '#fff',
                      cursor: 'pointer'
                    }}
                  >
                    {amount.toLocaleString()}P
                  </button>
                ))}
              </div>
            </div>

            <div style={{ display: 'flex', gap: '1rem' }}>
              <button
                onClick={() => setShowChargeModal(false)}
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
            </div>
          </div>
        </div>
      )}

      {/* 구독권 구매 모달 */}
      {showSubscriptionModal && (
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
            <h3 style={{ marginBottom: '1.5rem', color: '#333' }}>월 구독권 구매</h3>
            
            <div style={{
              padding: '1.5rem',
              backgroundColor: '#f8f9fa',
              borderRadius: '8px',
              marginBottom: '1.5rem'
            }}>
              <h4 style={{ color: '#333', marginBottom: '1rem' }}>구독권 혜택</h4>
              <ul style={{ color: '#666', paddingLeft: '1.2rem' }}>
                <li>30일간 모든 도서 무제한 읽기</li>
                <li>신작 도서 우선 열람</li>
                <li>광고 없는 독서 환경</li>
              </ul>
            </div>

            <div style={{
              display: 'flex',
              justifyContent: 'space-between',
              marginBottom: '1.5rem',
              fontSize: '1.1rem'
            }}>
              <span>가격:</span>
              <span style={{ fontWeight: 'bold', color: '#28a745' }}>9,900P</span>
            </div>

            <div style={{
              display: 'flex',
              justifyContent: 'space-between',
              marginBottom: '1.5rem',
              fontSize: '0.9rem',
              color: userInfo.points >= 9900 ? '#28a745' : '#dc3545'
            }}>
              <span>보유 포인트:</span>
              <span>{userInfo.points.toLocaleString()}P</span>
            </div>

            <div style={{ display: 'flex', gap: '1rem' }}>
              <button
                onClick={() => setShowSubscriptionModal(false)}
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
                onClick={handleSubscriptionPurchase}
                style={{
                  flex: 1,
                  padding: '0.8rem',
                  backgroundColor: userInfo.points >= 9900 ? '#28a745' : '#6c757d',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: userInfo.points >= 9900 ? 'pointer' : 'not-allowed'
                }}
                disabled={userInfo.points < 9900}
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

export default ReaderMyPage;