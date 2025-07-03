import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthorMyPage = () => {
  const navigate = useNavigate();
  
  // 작가 정보
  const [authorInfo, setAuthorInfo] = useState({
    name: '김작가',
    email: 'author@example.com',
    introduction: '소설과 에세이를 주로 쓰는 작가입니다. 일상에서 찾은 작은 이야기들을 글로 풀어내는 것을 좋아합니다.',
    representativeWork: '시간의 틈',
    joinDate: '2024-03-10',
    totalSales: 125000,
    totalBooks: 5
  });

  // 작가의 도서 목록
  const [authorBooks, setAuthorBooks] = useState([
    {
      id: 1,
      title: '시간의 틈',
      description: '시간 여행을 소재로 한 SF 소설',
      price: 4800,
      publishDate: '2024-06-01',
      sales: 45,
      status: '판매중',
      totalRevenue: 216000
    },
    {
      id: 2,
      title: '도시의 기억',
      description: '도시에서 살아가는 사람들의 이야기',
      price: 5200,
      publishDate: '2024-08-15',
      sales: 32,
      status: '판매중',
      totalRevenue: 166400
    },
    {
      id: 3,
      title: '바람의 노래',
      description: '자연과 인간의 관계를 다룬 에세이',
      price: 4500,
      publishDate: '2025-01-20',
      sales: 18,
      status: '판매중',
      totalRevenue: 81000
    }
  ]);

  const getStatusColor = (status) => {
    switch (status) {
      case '판매중': return '#28a745';
      case '검토중': return '#ffc107';
      case '판매중지': return '#dc3545';
      default: return '#6c757d';
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
          </nav>
        </div>
      </header>

      <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '2rem' }}>
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
            onClick={() => navigate('/bookRegister')}
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
                    {authorBooks.reduce((sum, book) => sum + book.sales, 0)}권
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
                    {authorBooks.reduce((sum, book) => sum + book.totalRevenue, 0).toLocaleString()}P
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
                            {book.description}
                          </p>
                          <p style={{ color: '#999', fontSize: '0.8rem' }}>
                            등록일: {new Date(book.publishDate).toLocaleDateString()}
                          </p>
                        </div>
                        <div style={{
                          backgroundColor: getStatusColor(book.status),
                          color: '#fff',
                          padding: '0.3rem 0.8rem',
                          borderRadius: '20px',
                          fontSize: '0.8rem',
                          fontWeight: '500'
                        }}>
                          {book.status}
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
                            {book.price.toLocaleString()}P
                          </div>
                        </div>
                        <div style={{ textAlign: 'center' }}>
                          <div style={{ color: '#666', fontSize: '0.8rem', marginBottom: '0.3rem' }}>
                            판매량
                          </div>
                          <div style={{ fontWeight: 'bold', color: '#007bff' }}>
                            {book.sales}권
                          </div>
                        </div>
                        <div style={{ textAlign: 'center' }}>
                          <div style={{ color: '#666', fontSize: '0.8rem', marginBottom: '0.3rem' }}>
                            수익
                          </div>
                          <div style={{ fontWeight: 'bold', color: '#28a745' }}>
                            {book.totalRevenue.toLocaleString()}P
                          </div>
                        </div>
                        <div style={{ textAlign: 'center' }}>
                          <button style={{
                            padding: '0.4rem 0.8rem',
                            backgroundColor: '#6c757d',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '4px',
                            cursor: 'pointer',
                            fontSize: '0.8rem'
                          }}>
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
                    onClick={() => navigate('/bookRegister')}
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
  );
};

export default AuthorMyPage;