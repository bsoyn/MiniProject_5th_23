import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const BASE_URL = process.env.REACT_APP_API_BASE_URL;

const AdminPage = () => {
  const navigate = useNavigate();
  const [pendingAuthors, setPendingAuthors] = useState([]);
  const [approvedAuthors, setApprovedAuthors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedAuthor, setSelectedAuthor] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [actionType, setActionType] = useState(''); // 'approve' or 'deny'
  const [activeTab, setActiveTab] = useState('pending'); // 'pending' or 'approved'

  // 페이지 로드 시 작가 목록 가져오기
  useEffect(() => {
    fetchPendingAuthors();
    fetchApprovedAuthors();
  }, []);

  // 승인 대기 작가 목록 조회
  const fetchPendingAuthors = async () => {
    try {
      const response = await fetch(`${BASE_URL}/manageAuthors`);
      
      if (response.ok) {
        const data = await response.json();
        setPendingAuthors(data);
        setError(null);
      } else {
        setError('승인 대기 작가 목록을 불러오는데 실패했습니다.');
      }
    } catch (error) {
      console.error('승인 대기 작가 목록 조회 실패:', error);
      setError('서버 연결에 실패했습니다.');
    }
  };

  // 승인된 작가 목록 조회
  const fetchApprovedAuthors = async () => {
    try {
      const response = await fetch(`${BASE_URL}/authors`);
      
      if (response.ok) {
        const data = await response.json();
        setApprovedAuthors(data);
      } else {
        console.error('승인된 작가 목록 조회 실패');
      }
    } catch (error) {
      console.error('승인된 작가 목록 조회 실패:', error);
    } finally {
      setLoading(false);
    }
  };

  // 작가 승인/거부 모달 열기
  const openActionModal = (author, action) => {
    setSelectedAuthor(author);
    setActionType(action);
    setShowModal(true);
  };

  // 작가 승인/거부 처리
  const handleAuthorAction = async () => {
    if (!selectedAuthor) return;

    try {
      const endpoint = actionType === 'approve' 
        ? `${BASE_URL}/manageAuthors/${selectedAuthor.id}/approve`
        : `${BASE_URL}/manageAuthors/${selectedAuthor.id}/deny`;

      const response = await fetch(endpoint, {
        method: 'PATCH'
      });

      if (response.ok) {
        const result = await response.json();
        alert(actionType === 'approve' 
          ? `${selectedAuthor.name} 작가가 승인되었습니다.`
          : `${selectedAuthor.name} 작가가 거부되었습니다.`
        );
        
        // 목록 새로고침
        fetchPendingAuthors();
        fetchApprovedAuthors();
        setShowModal(false);
        setSelectedAuthor(null);
      } else {
        alert('작업 처리에 실패했습니다.');
      }
    } catch (error) {
      console.error('작가 승인/거부 처리 실패:', error);
      alert('서버 연결에 실패했습니다.');
    }
  };

  // 모달 닫기
  const closeModal = () => {
    setShowModal(false);
    setSelectedAuthor(null);
    setActionType('');
  };

  // 로그아웃
  const handleLogout = () => {
    sessionStorage.removeItem('accessToken');
    navigate('/login');
  };

  if (loading) {
    return (
      <div style={{
        minHeight: '100vh',
        backgroundColor: '#f8f9fa',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        fontFamily: 'Arial, sans-serif'
      }}>
        <div style={{
          fontSize: '1.2rem',
          color: '#666'
        }}>
          로딩 중...
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
            BookHub 관리자
          </h1>
          <nav style={{ display: 'flex', gap: '1rem' }}>
            <button
              onClick={handleLogout}
              style={{
                padding: '0.5rem 1rem',
                backgroundColor: '#dc3545',
                color: 'white',
                border: 'none',
                borderRadius: '4px',
                cursor: 'pointer',
                fontSize: '0.9rem'
              }}
            >
              로그아웃
            </button>
          </nav>
        </div>
      </header>

      {/* 메인 컨텐츠 */}
      <main style={{
        maxWidth: '1200px',
        margin: '0 auto',
        padding: '2rem'
      }}>
        {/* 통계 카드 */}
        <div style={{
          display: 'flex',
          gap: '1rem',
          marginBottom: '2rem'
        }}>
          <div style={{
            backgroundColor: '#fff',
            padding: '1.5rem',
            borderRadius: '8px',
            boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
            textAlign: 'center',
            minWidth: '150px',
            cursor: 'pointer',
            border: activeTab === 'pending' ? '2px solid #dc3545' : '2px solid transparent'
          }}
          onClick={() => setActiveTab('pending')}
          >
            <h3 style={{
              margin: '0 0 0.5rem 0',
              color: '#666',
              fontSize: '0.9rem',
              textTransform: 'uppercase'
            }}>
              승인 대기
            </h3>
            <p style={{
              fontSize: '2.5rem',
              fontWeight: 'bold',
              color: '#dc3545',
              margin: '0.25rem 0 0 0'
            }}>
              {pendingAuthors.length}
            </p>
            <p style={{
              margin: '0.25rem 0 0 0',
              color: '#999',
              fontSize: '0.8rem'
            }}>
              작가
            </p>
          </div>

          <div style={{
            backgroundColor: '#fff',
            padding: '1.5rem',
            borderRadius: '8px',
            boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
            textAlign: 'center',
            minWidth: '150px',
            cursor: 'pointer',
            border: activeTab === 'approved' ? '2px solid #28a745' : '2px solid transparent'
          }}
          onClick={() => setActiveTab('approved')}
          >
            <h3 style={{
              margin: '0 0 0.5rem 0',
              color: '#666',
              fontSize: '0.9rem',
              textTransform: 'uppercase'
            }}>
              승인된 작가
            </h3>
            <p style={{
              fontSize: '2.5rem',
              fontWeight: 'bold',
              color: '#28a745',
              margin: '0.25rem 0 0 0'
            }}>
              {approvedAuthors.length}
            </p>
            <p style={{
              margin: '0.25rem 0 0 0',
              color: '#999',
              fontSize: '0.8rem'
            }}>
              작가
            </p>
          </div>
        </div>

        {/* 탭 네비게이션 */}
        <div style={{
          display: 'flex',
          marginBottom: '2rem',
          borderBottom: '2px solid #e0e0e0'
        }}>
          <button
            onClick={() => setActiveTab('pending')}
            style={{
              padding: '1rem 2rem',
              backgroundColor: activeTab === 'pending' ? '#dc3545' : 'transparent',
              color: activeTab === 'pending' ? 'white' : '#666',
              border: 'none',
              borderBottom: activeTab === 'pending' ? '2px solid #dc3545' : '2px solid transparent',
              cursor: 'pointer',
              fontSize: '1rem',
              fontWeight: activeTab === 'pending' ? 'bold' : 'normal',
              marginBottom: '-2px'
            }}
          >
            승인 대기 작가 ({pendingAuthors.length})
          </button>
          <button
            onClick={() => setActiveTab('approved')}
            style={{
              padding: '1rem 2rem',
              backgroundColor: activeTab === 'approved' ? '#28a745' : 'transparent',
              color: activeTab === 'approved' ? 'white' : '#666',
              border: 'none',
              borderBottom: activeTab === 'approved' ? '2px solid #28a745' : '2px solid transparent',
              cursor: 'pointer',
              fontSize: '1rem',
              fontWeight: activeTab === 'approved' ? 'bold' : 'normal',
              marginBottom: '-2px'
            }}
          >
            승인된 작가 ({approvedAuthors.length})
          </button>
        </div>

        {/* 작가 목록 */}
        <section style={{
          backgroundColor: '#fff',
          borderRadius: '8px',
          padding: '2rem',
          boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
        }}>
          <h2 style={{
            margin: '0 0 1.5rem 0',
            color: '#333',
            fontSize: '1.5rem',
            borderBottom: `2px solid ${activeTab === 'pending' ? '#dc3545' : '#28a745'}`,
            paddingBottom: '0.5rem'
          }}>
            {activeTab === 'pending' ? '승인 대기 작가 목록' : '승인된 작가 목록'}
          </h2>
          
          {error && (
            <div style={{
              backgroundColor: '#f8d7da',
              color: '#721c24',
              padding: '0.8rem',
              borderRadius: '4px',
              marginBottom: '1rem',
              fontSize: '0.9rem',
              border: '1px solid #f5c6cb'
            }}>
              {error}
            </div>
          )}

          {activeTab === 'pending' ? (
            // 승인 대기 작가 목록
            pendingAuthors.length === 0 ? (
              <div style={{
                textAlign: 'center',
                padding: '3rem',
                color: '#666',
                fontSize: '1.1rem'
              }}>
                승인 대기 중인 작가가 없습니다.
              </div>
            ) : (
              <div style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fill, minmax(350px, 1fr))',
                gap: '1.5rem'
              }}>
                {pendingAuthors.map((author) => (
                  <div key={author.id} style={{
                    border: '1px solid #e0e0e0',
                    borderRadius: '8px',
                    padding: '1.5rem',
                    backgroundColor: '#fafafa'
                  }}>
                    <div>
                      <h3 style={{
                        margin: '0 0 0.5rem 0',
                        color: '#333',
                        fontSize: '1.2rem'
                      }}>
                        {author.name}
                      </h3>
                      <p style={{
                        color: '#666',
                        margin: '0.25rem 0',
                        fontSize: '0.9rem'
                      }}>
                        {author.email}
                      </p>
                      <p style={{
                        color: '#555',
                        margin: '0.75rem 0',
                        lineHeight: '1.4',
                        fontSize: '0.9rem'
                      }}>
                        {author.bio}
                      </p>
                      <p style={{
                        color: '#666',
                        margin: '0.5rem 0',
                        fontSize: '0.9rem'
                      }}>
                        <strong>대표작:</strong> {author.majorWork}
                      </p>
                    </div>
                    
                    <div style={{
                      display: 'flex',
                      gap: '0.5rem',
                      marginTop: '1rem',
                      paddingTop: '1rem',
                      borderTop: '1px solid #e0e0e0'
                    }}>
                      <button
                        onClick={() => openActionModal(author, 'approve')}
                        style={{
                          flex: 1,
                          padding: '0.5rem',
                          backgroundColor: '#28a745',
                          color: 'white',
                          border: 'none',
                          borderRadius: '4px',
                          cursor: 'pointer',
                          fontSize: '0.9rem'
                        }}
                      >
                        승인
                      </button>
                      <button
                        onClick={() => openActionModal(author, 'deny')}
                        style={{
                          flex: 1,
                          padding: '0.5rem',
                          backgroundColor: '#dc3545',
                          color: 'white',
                          border: 'none',
                          borderRadius: '4px',
                          cursor: 'pointer',
                          fontSize: '0.9rem'
                        }}
                      >
                        거부
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            )
          ) : (
            // 승인된 작가 목록
            approvedAuthors.length === 0 ? (
              <div style={{
                textAlign: 'center',
                padding: '3rem',
                color: '#666',
                fontSize: '1.1rem'
              }}>
                승인된 작가가 없습니다.
              </div>
            ) : (
              <div style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fill, minmax(350px, 1fr))',
                gap: '1.5rem'
              }}>
                {approvedAuthors.map((author) => (
                  <div key={author.id} style={{
                    border: '1px solid #e0e0e0',
                    borderRadius: '8px',
                    padding: '1.5rem',
                    backgroundColor: '#f8fff8'
                  }}>
                    <div>
                      <h3 style={{
                        margin: '0 0 0.5rem 0',
                        color: '#333',
                        fontSize: '1.2rem'
                      }}>
                        {author.name}
                      </h3>
                      <p style={{
                        color: '#666',
                        margin: '0.25rem 0',
                        fontSize: '0.9rem'
                      }}>
                        {author.email}
                      </p>
                      <p style={{
                        color: '#555',
                        margin: '0.75rem 0',
                        lineHeight: '1.4',
                        fontSize: '0.9rem'
                      }}>
                        {author.bio}
                      </p>
                      <p style={{
                        color: '#666',
                        margin: '0.5rem 0',
                        fontSize: '0.9rem'
                      }}>
                        <strong>대표작:</strong> {author.majorWork}
                      </p>
                      <div style={{
                        marginTop: '0.5rem',
                        padding: '0.25rem 0.5rem',
                        backgroundColor: '#28a745',
                        color: 'white',
                        borderRadius: '4px',
                        fontSize: '0.8rem',
                        display: 'inline-block'
                      }}>
                        승인됨
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )
          )}
        </section>
      </main>

      {/* 승인/거부 확인 모달 */}
      {showModal && selectedAuthor && (
        <div 
          onClick={closeModal}
          style={{
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: 'rgba(0, 0, 0, 0.5)',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            zIndex: 1000
          }}
        >
          <div 
            onClick={(e) => e.stopPropagation()}
            style={{
              backgroundColor: 'white',
              padding: '2rem',
              borderRadius: '8px',
              maxWidth: '400px',
              width: '90%',
              textAlign: 'center',
              boxShadow: '0 4px 20px rgba(0, 0, 0, 0.3)'
            }}
          >
            <h3 style={{
              margin: '0 0 1rem 0',
              color: '#333'
            }}>
              작가 {actionType === 'approve' ? '승인' : '거부'} 확인
            </h3>
            <p style={{
              margin: '0 0 1.5rem 0',
              color: '#666',
              lineHeight: '1.5'
            }}>
              <strong>{selectedAuthor.name}</strong> 작가를{' '}
              {actionType === 'approve' ? '승인' : '거부'}하시겠습니까?
            </p>
            <div style={{
              display: 'flex',
              gap: '1rem',
              justifyContent: 'center'
            }}>
              <button
                onClick={handleAuthorAction}
                style={{
                  padding: '0.5rem 1rem',
                  backgroundColor: actionType === 'approve' ? '#28a745' : '#dc3545',
                  color: 'white',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer'
                }}
              >
                {actionType === 'approve' ? '승인' : '거부'}
              </button>
              <button 
                onClick={closeModal}
                style={{
                  padding: '0.5rem 1rem',
                  backgroundColor: '#6c757d',
                  color: 'white',
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
    </div>
  );
};

export default AdminPage; 