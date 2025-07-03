import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

const BookPurchasePage = () => {
  const navigate = useNavigate();
  const { bookId } = useParams();
  
  // 상태 관리
  const [book, setBook] = useState(null);
  const [user, setUser] = useState({ 
    type: 'reader', 
    name: '홍길동', 
    points: 1000000, // 현재 보유 포인트
    id: 1 
  });
  const [loading, setLoading] = useState(true);
  const [purchaseStep, setPurchaseStep] = useState('confirm'); // confirm, payment, complete
  const [paymentMethod, setPaymentMethod] = useState('points'); // points, charge
  const [chargeAmount, setChargeAmount] = useState('');
  const [agreeTerms, setAgreeTerms] = useState(false);
  const [isProcessing, setIsProcessing] = useState(false);

  // 임시 도서 데이터
  const sampleBook = {
    id: parseInt(bookId),
    title: '별을 삼킨 소년',
    author: '김작가',
    category: 'SF',
    price: 5000,
    imageUrl: 'https://via.placeholder.com/300x420/333333/ffffff?text=별을+삼킨+소년',
    description: '소년은 어느 조용한 밤, 마당에 떨어진 작은 별 하나를 발견했다.',
    rating: 4.7,
    reviewCount: 89,
    pages: 180
  };

  // 충전 금액 옵션
  const chargeOptions = [
    { amount: 5000, bonus: 0, label: '5,000P' },
    { amount: 10000, bonus: 500, label: '10,000P (+500P 보너스)' },
    { amount: 20000, bonus: 1500, label: '20,000P (+1,500P 보너스)' },
    { amount: 50000, bonus: 5000, label: '50,000P (+5,000P 보너스)' }
  ];

  useEffect(() => {
    const loadBook = async () => {
      setLoading(true);
      try {
        // 실제로는 API 호출
        await new Promise(resolve => setTimeout(resolve, 1000));
        setBook(sampleBook);
      } catch (error) {
        console.error('도서 정보 불러오기 실패:', error);
      } finally {
        setLoading(false);
      }
    };

    loadBook();
  }, [bookId]);

  // 포인트로 구매
  const handlePointsPurchase = async () => {
    if (user.points < book.price) {
      alert('포인트가 부족합니다. 포인트를 충전해주세요.');
      setPaymentMethod('charge');
      return;
    }

    if (!agreeTerms) {
      alert('구매 약관에 동의해주세요.');
      return;
    }

    setIsProcessing(true);
    
    try {
      // 실제로는 구매 API 호출
      await new Promise(resolve => setTimeout(resolve, 2000));
      
      // 포인트 차감
      setUser(prev => ({
        ...prev,
        points: prev.points - book.price
      }));
      
      setPurchaseStep('complete');
    } catch (error) {
      alert('구매 중 오류가 발생했습니다.');
    } finally {
      setIsProcessing(false);
    }
  };

  // 포인트 충전 후 구매
  const handleChargeAndPurchase = async () => {
    const amount = parseInt(chargeAmount);
    if (!amount || amount < (book.price - user.points)) {
      alert(`최소 ${(book.price - user.points).toLocaleString()}P 이상 충전해주세요.`);
      return;
    }

    if (!agreeTerms) {
      alert('구매 약관에 동의해주세요.');
      return;
    }

    setIsProcessing(true);
    
    try {
      // 1. 포인트 충전 API 호출
      await new Promise(resolve => setTimeout(resolve, 2000));
      
      // 보너스 포인트 계산
      const selectedOption = chargeOptions.find(option => option.amount === amount);
      const bonusPoints = selectedOption ? selectedOption.bonus : 0;
      const totalCharged = amount + bonusPoints;
      
      // 2. 포인트 추가 및 도서 구매
      setUser(prev => ({
        ...prev,
        points: prev.points + totalCharged - book.price
      }));
      
      setPurchaseStep('complete');
    } catch (error) {
      alert('결제 중 오류가 발생했습니다.');
    } finally {
      setIsProcessing(false);
    }
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
            <span style={{ color: '#666', fontSize: '0.9rem' }}>
              {user.name}님 (보유: {user.points.toLocaleString()}P)
            </span>
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
                  저자: {book.author}
                </p>
                <p style={{ color: '#666', marginBottom: '0.5rem' }}>
                  카테고리: {book.category}
                </p>
                <p style={{ color: '#666', marginBottom: '1rem' }}>
                  페이지: {book.pages}쪽
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
              backgroundColor: user.points >= book.price ? '#e8f5e8' : '#fff3cd',
              border: user.points >= book.price ? '1px solid #d4edda' : '1px solid #ffeaa7',
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
                <span style={{ fontWeight: 'bold' }}>{user.points.toLocaleString()}P</span>
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
                  color: user.points >= book.price ? '#28a745' : '#dc3545' 
                }}>
                  {user.points >= book.price 
                    ? `${(user.points - book.price).toLocaleString()}P` 
                    : `${(user.points - book.price).toLocaleString()}P (부족)`
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
                  border: paymentMethod === 'points' ? '2px solid #007bff' : '1px solid #ddd',
                  borderRadius: '8px',
                  cursor: user.points >= book.price ? 'pointer' : 'not-allowed',
                  backgroundColor: paymentMethod === 'points' ? '#f0f8ff' : '#fff',
                  opacity: user.points >= book.price ? 1 : 0.6
                }}>
                  <input
                    type="radio"
                    name="paymentMethod"
                    value="points"
                    checked={paymentMethod === 'points'}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                    disabled={user.points < book.price}
                    style={{ marginRight: '1rem' }}
                  />
                  <div>
                    <div style={{ fontWeight: '500', color: '#333' }}>
                      보유 포인트로 결제
                    </div>
                    <div style={{ fontSize: '0.9rem', color: '#666' }}>
                      현재 {user.points.toLocaleString()}P 보유 중
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
                      부족한 포인트를 충전하고 구매
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
                onClick={() => navigate(`/books/${bookId}`)}
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
              <button
                onClick={() => setPurchaseStep('payment')}
                disabled={!agreeTerms}
                style={{
                  flex: 2,
                  padding: '1rem',
                  backgroundColor: agreeTerms ? '#007bff' : '#ddd',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '8px',
                  cursor: agreeTerms ? 'pointer' : 'not-allowed',
                  fontSize: '1rem',
                  fontWeight: 'bold'
                }}
              >
                다음 단계
              </button>
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

            {paymentMethod === 'points' ? (
              // 포인트 결제
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
                    <span>{user.points.toLocaleString()}P</span>
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
                      {(user.points - book.price).toLocaleString()}P
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
            ) : (
              // 포인트 충전 후 결제
              <div>
                <h3 style={{ color: '#333', marginBottom: '1.5rem' }}>포인트 충전</h3>
                
                {/* 부족한 포인트 안내 */}
                <div style={{
                  padding: '1rem',
                  backgroundColor: '#fff3cd',
                  border: '1px solid #ffeaa7',
                  borderRadius: '8px',
                  marginBottom: '2rem'
                }}>
                  <div style={{ fontSize: '0.9rem', color: '#856404' }}>
                    현재 {user.points.toLocaleString()}P 보유 중이며, 
                    {(book.price - user.points).toLocaleString()}P가 부족합니다.
                  </div>
                </div>

                {/* 충전 금액 선택 */}
                <div style={{ marginBottom: '2rem' }}>
                  <h4 style={{ color: '#333', marginBottom: '1rem' }}>충전 금액 선택</h4>
                  <div style={{
                    display: 'grid',
                    gridTemplateColumns: 'repeat(2, 1fr)',
                    gap: '1rem',
                    marginBottom: '1rem'
                  }}>
                    {chargeOptions.map(option => (
                      <button
                        key={option.amount}
                        onClick={() => setChargeAmount(option.amount.toString())}
                        style={{
                          padding: '1rem',
                          border: chargeAmount === option.amount.toString() ? '2px solid #007bff' : '1px solid #ddd',
                          borderRadius: '8px',
                          backgroundColor: chargeAmount === option.amount.toString() ? '#f0f8ff' : '#fff',
                          cursor: 'pointer',
                          textAlign: 'left'
                        }}
                      >
                        <div style={{ fontWeight: 'bold', marginBottom: '0.3rem' }}>
                          {option.amount.toLocaleString()}P
                        </div>
                        <div style={{ fontSize: '0.9rem', color: '#666' }}>
                          {option.label}
                        </div>
                      </button>
                    ))}
                  </div>

                  {/* 직접 입력 */}
                  <div>
                    <label style={{
                      display: 'block',
                      marginBottom: '0.5rem',
                      fontWeight: '500',
                      color: '#333'
                    }}>
                      직접 입력 (최소 {(book.price - user.points).toLocaleString()}P)
                    </label>
                    <input
                      type="number"
                      value={chargeAmount}
                      onChange={(e) => setChargeAmount(e.target.value)}
                      placeholder="충전할 포인트를 입력하세요"
                      min={book.price - user.points}
                      style={{
                        width: '100%',
                        padding: '0.8rem',
                        border: '1px solid #ddd',
                        borderRadius: '4px',
                        fontSize: '1rem',
                        outline: 'none',
                        boxSizing: 'border-box'
                      }}
                    />
                  </div>
                </div>

                {/* 결제 후 포인트 현황 */}
                {chargeAmount && (
                  <div style={{
                    padding: '1.5rem',
                    backgroundColor: '#f8f9fa',
                    borderRadius: '8px',
                    marginBottom: '2rem'
                  }}>
                    <h4 style={{ color: '#333', marginBottom: '1rem' }}>결제 후 포인트 현황</h4>
                    <div style={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      marginBottom: '0.5rem'
                    }}>
                      <span>현재 보유</span>
                      <span>{user.points.toLocaleString()}P</span>
                    </div>
                    <div style={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      marginBottom: '0.5rem'
                    }}>
                      <span>충전 포인트</span>
                      <span>+{parseInt(chargeAmount || 0).toLocaleString()}P</span>
                    </div>
                    {(() => {
                      const selectedOption = chargeOptions.find(option => option.amount === parseInt(chargeAmount));
                      return selectedOption && selectedOption.bonus > 0 ? (
                        <div style={{
                          display: 'flex',
                          justifyContent: 'space-between',
                          marginBottom: '0.5rem',
                          color: '#28a745'
                        }}>
                          <span>보너스 포인트</span>
                          <span>+{selectedOption.bonus.toLocaleString()}P</span>
                        </div>
                      ) : null;
                    })()}
                    <div style={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      marginBottom: '0.5rem'
                    }}>
                      <span>도서 구매</span>
                      <span>-{book.price.toLocaleString()}P</span>
                    </div>
                    <hr style={{ margin: '1rem 0' }} />
                    <div style={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      fontSize: '1.1rem',
                      fontWeight: 'bold'
                    }}>
                      <span>최종 잔액</span>
                      <span style={{ color: '#28a745' }}>
                        {(() => {
                          const amount = parseInt(chargeAmount || 0);
                          const selectedOption = chargeOptions.find(option => option.amount === amount);
                          const bonus = selectedOption ? selectedOption.bonus : 0;
                          return (user.points + amount + bonus - book.price).toLocaleString();
                        })()}P
                      </span>
                    </div>
                  </div>
                )}

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
                    onClick={handleChargeAndPurchase}
                    disabled={isProcessing || !chargeAmount || parseInt(chargeAmount) < (book.price - user.points)}
                    style={{
                      flex: 2,
                      padding: '1rem',
                      backgroundColor: (isProcessing || !chargeAmount || parseInt(chargeAmount) < (book.price - user.points)) ? '#ddd' : '#007bff',
                      color: '#fff',
                      border: 'none',
                      borderRadius: '8px',
                      cursor: (isProcessing || !chargeAmount || parseInt(chargeAmount) < (book.price - user.points)) ? 'not-allowed' : 'pointer',
                      fontSize: '1rem',
                      fontWeight: 'bold'
                    }}
                  >
                    {isProcessing ? '결제 처리 중...' : '충전 후 구매하기'}
                  </button>
                </div>
              </div>
            )}
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
                <span>{book.author}</span>
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
                <span style={{ color: '#007bff' }}>{user.points.toLocaleString()}P</span>
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
                onClick={() => navigate(`/books/${bookId}`)}
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
                onClick={() => navigate('/reader-mypage')}
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
                onClick={() => navigate('/books')}
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