import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import apiClient from '../client.js';

const BookRegisterPage = () => {
  const navigate = useNavigate();
  
  // 사용자 정보 상태
  const [userInfo, setUserInfo] = useState({
    userId: null,
    userName: null,
    isLoading: true
  });
  
  // 도서 정보 상태
  const [bookInfo, setBookInfo] = useState({
    title: '',
    content: '',
    penName: '', // 필명 추가
    // AI가 생성할 정보들
    cover: null,
    category: '',
    suggestedPrice: null,
    // 추가 정보
    description: '',
    manuscriptId: null // AI 분석 후 받을 manuscript ID
  });

  // 임시 저장된 원고 목록
  const [savedDrafts, setSavedDrafts] = useState([]);

  // UI 상태
  const [activeTab, setActiveTab] = useState('new'); // 'new' or 'drafts'
  const [isGenerating, setIsGenerating] = useState(false);
  const [hasGenerated, setHasGenerated] = useState(false);
  const [errors, setErrors] = useState({});
  const [showSaveModal, setShowSaveModal] = useState(false);
  const [draftTitle, setDraftTitle] = useState('');
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [draftToDelete, setDraftToDelete] = useState(null);

  // 토큰에서 사용자 정보 가져오기
  const fetchUserInfo = async () => {
    try {
      const accessToken = sessionStorage.getItem('accessToken');
      
      if (!accessToken) {
        alert('로그인이 필요합니다.');
        navigate('/login');
        return;
      }

      const response = await apiClient.post('/api/token', {}, {
        headers: {
          'Authorization': `Bearer ${accessToken}`
        }
      });

      setUserInfo({
        userId: response.userId,
        userName: response.userName,
        isLoading: false
      });

    } catch (error) {
      console.error('사용자 정보 가져오기 실패:', error);
      
      // 토큰이 유효하지 않은 경우
      if (error.response?.status === 401 || error.response?.status === 403) {
        sessionStorage.removeItem('accessToken');
        alert('로그인이 만료되었습니다. 다시 로그인해주세요.');
        navigate('/login');
      } else {
        alert('사용자 정보를 가져오는데 실패했습니다.');
        setUserInfo({
          userId: null,
          userName: null,
          isLoading: false
        });
      }
    }
  };

  // 입력 핸들러
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setBookInfo(prev => ({
      ...prev,
      [name]: value
    }));

    // 에러 메시지 제거
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  // 폼 유효성 검사
  const validateForm = () => {
    const newErrors = {};
    
    if (!bookInfo.title.trim()) {
      newErrors.title = '제목을 입력해주세요';
    }
    if (!bookInfo.content.trim()) {
      newErrors.content = '내용을 입력해주세요';
    }
    if (!bookInfo.penName.trim()) {
      newErrors.penName = '필명을 입력해주세요';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // AI 분석 및 생성 함수
  const generateAIContent = async () => {
    if (!validateForm()) {
      return;
    }

    if (!userInfo.userId) {
      alert('사용자 정보를 불러오는 중입니다. 잠시 후 다시 시도해주세요.');
      return;
    }

    setIsGenerating(true);
    setErrors({});

    try {
      // 1단계: AI 분석 요청 - imageUrl, category, price 생성
      const manuscript = await apiClient.post('/manuscripts/request-publication', {
        authorId: userInfo.userId,
        title: bookInfo.title,
        contents: bookInfo.content
      });

      const manuscriptId = manuscript.id;

      // 2단계: AI 처리 완료까지 폴링
      const maxAttempts = 60;
      let attempts = 0;
      
      const pollForAIResult = async () => {
        try {
          const aiResult = await apiClient.get(`/manuscripts/${manuscriptId}`);
          
          // AI 속성들이 모두 생성되었는지 확인
          if (aiResult.imageUrl && aiResult.category && aiResult.price) {
            // AI 결과로 상태 업데이트
            setBookInfo(prev => ({
              ...prev,
              cover: aiResult.imageUrl,
              category: aiResult.category,
              suggestedPrice: aiResult.price,
              description: `"${aiResult.title}"은 ${aiResult.category} 장르의 흥미진진한 작품입니다.`,
              manuscriptId: manuscriptId
            }));
            
            setHasGenerated(true);
            setIsGenerating(false);
            return true;
          } else if (attempts < maxAttempts) {
            attempts++;
            setTimeout(pollForAIResult, 2000);
            return false;
          } else {
            throw new Error('AI 처리 시간이 초과되었습니다.');
          }
        } catch (error) {
          if (attempts < maxAttempts) {
            attempts++;
            setTimeout(pollForAIResult, 1000);
            return false;
          } else {
            throw error;
          }
        }
      };

      await pollForAIResult();
    } catch (error) {
      console.error('AI 생성 중 오류:', error);
      const errorMessage = error.response?.data?.message || error.message || '알 수 없는 오류가 발생했습니다';
      alert(`AI 분석 중 오류가 발생했습니다: ${errorMessage}`);
      setIsGenerating(false);
    }
  };

  // 임시 저장
  const saveDraft = async () => {
    if (!bookInfo.title.trim() && !bookInfo.content.trim()) {
      alert('제목이나 내용 중 하나 이상 입력해주세요.');
      return;
    }

    if (!userInfo.userId) {
      alert('사용자 정보를 불러오는 중입니다. 잠시 후 다시 시도해주세요.');
      return;
    }

    try {
      const savedManuscript = await apiClient.post('/manuscripts/temp-save', {
        manuscriptId: null,
        title: draftTitle || bookInfo.title || '제목 없음',
        contents: bookInfo.content,
        authorId: userInfo.userId
      });
      
      // 로컬 상태 업데이트
      const newDraft = {
        id: savedManuscript.id,
        title: savedManuscript.title,
        content: savedManuscript.contents,
        saveDate: new Date().toISOString().split('T')[0],
        wordCount: savedManuscript.contents.length
      };

      setSavedDrafts(prev => [newDraft, ...prev]);
      setShowSaveModal(false);
      setDraftTitle('');
      alert('임시 저장되었습니다.');
    } catch (error) {
      console.error('임시 저장 중 오류:', error);
      const errorMessage = error.response?.data?.message || error.message || '알 수 없는 오류가 발생했습니다';
      alert(`저장 실패: ${errorMessage}`);
    }
  };

  // 임시 저장된 원고 불러오기
  const loadDraft = async (draft) => {
    try {
      // 특정 임시 저장 원고 상세 정보 가져오기
      const draftData = await apiClient.get(`/manuscripts/${draft.id}`);
      
      console.log('불러온 원고 데이터:', draftData);
      
      setBookInfo(prev => ({
        ...prev,
        title: draftData.title || draft.title,
        content: draftData.content || draftData.contents || draft.content,
        penName: '',
        // AI 생성 정보는 초기화
        cover: null,
        category: '',
        suggestedPrice: null,
        description: '',
        manuscriptId: null
      }));
      
      setHasGenerated(false);
      setActiveTab('new');
      alert('원고를 불러왔습니다.');
    } catch (error) {
      console.error('원고 불러오기 중 오류:', error);
      
      // API 실패 시 로컬 데이터 사용
      setBookInfo(prev => ({
        ...prev,
        title: draft.title,
        content: draft.content,
        penName: '',
        // AI 생성 정보는 초기화
        cover: null,
        category: '',
        suggestedPrice: null,
        description: '',
        manuscriptId: null
      }));
      
      setHasGenerated(false);
      setActiveTab('new');
      alert('원고를 불러왔습니다. (로컬 데이터 사용)');
    }
  };

  // 임시 저장된 원고 삭제
  const deleteDraft = async (draftId) => {
    try {
      // 실제로는 DELETE /manuscripts/temp/{id} API 필요
      // 현재는 로컬에서만 제거
      setSavedDrafts(prev => prev.filter(draft => draft.id !== draftId));
      setShowDeleteModal(false);
      setDraftToDelete(null);
      alert('원고가 삭제되었습니다.');
    } catch (error) {
      console.error('삭제 중 오류:', error);
      alert('삭제 중 오류가 발생했습니다.');
    }
  };

  // 삭제 확인 모달 열기
  const openDeleteModal = (draft) => {
    setDraftToDelete(draft);
    setShowDeleteModal(true);
  };

  // 최종 도서 등록
  const submitBook = async () => {
    console.log('submitBook 시작 - bookInfo:', bookInfo);
    
    if (!hasGenerated) {
      alert('먼저 AI 분석을 완료해주세요.');
      return;
    }

    if (!bookInfo.manuscriptId) {
      alert('AI 분석 결과가 없습니다. 다시 분석해주세요.');
      return;
    }

    if (!bookInfo.penName.trim()) {
      setErrors(prev => ({
        ...prev,
        penName: '필명을 입력해주세요'
      }));
      return;
    }

    if (!userInfo.userId) {
      alert('사용자 정보를 불러오는 중입니다. 잠시 후 다시 시도해주세요.');
      return;
    }

    try {
      console.log('API 호출 시작 - manuscriptId:', bookInfo.manuscriptId, 'penName:', bookInfo.penName);
      
      // ApiClient는 자동으로 JSON 파싱하므로 별도의 응답 처리 불필요
      const result = await apiClient.post(`/manuscripts/${bookInfo.manuscriptId}/complete-writing?penName=${bookInfo.penName}`, {});

      console.log('성공 응답:', result);
      alert('도서 등록이 완료되었습니다! 관리자 검토 후 판매가 시작됩니다.');
      
      // 상태 초기화
      setBookInfo({
        title: '',
        content: '',
        penName: '',
        cover: null,
        category: '',
        suggestedPrice: null,
        description: '',
        manuscriptId: null
      });
      setHasGenerated(false);
      
      navigate('/authorMypage');
    } catch (error) {
      console.error('도서 등록 중 오류:', error);
      const errorMessage = error.response?.data?.message || error.message || '알 수 없는 오류가 발생했습니다';
      alert(`등록 실패: ${errorMessage}`);
    }
  };

  // 임시 저장 목록 불러오기
  const loadTempManuscripts = async () => {
    if (!userInfo.userId) {
      return;
    }

    try {
      const manuscripts = await apiClient.get(`/manuscripts/temp?authorId=${userInfo.userId}`);
      
      console.log('불러온 임시 저장 목록:', manuscripts);
      
      const formattedDrafts = manuscripts.map(manuscript => ({
        id: manuscript.id,
        title: manuscript.title || '제목 없음',
        content: (manuscript.content || manuscript.contents || '').substring(0, 150),
        saveDate: manuscript.createdAt ? manuscript.createdAt.split('T')[0] : new Date().toISOString().split('T')[0],
        wordCount: (manuscript.content || manuscript.contents || '').length
      }));
      
      setSavedDrafts(formattedDrafts);
    } catch (error) {
      console.error('임시 저장 목록 불러오기 실패:', error);
      setSavedDrafts([]);
    }
  };

  // 컴포넌트 마운트시 사용자 정보 가져오기
  useEffect(() => {
    fetchUserInfo();
  }, []);

  // 사용자 정보가 로드되면 임시 저장 목록 불러오기
  useEffect(() => {
    if (userInfo.userId) {
      loadTempManuscripts();
    }
  }, [userInfo.userId]);

  // 사용자 정보 로딩 중일 때
  if (userInfo.isLoading) {
    return (
      <div style={{
        minHeight: '100vh',
        backgroundColor: '#f8f9fa',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center'
      }}>
        <div style={{
          textAlign: 'center',
          color: '#666'
        }}>
          <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>⏳</div>
          <p>사용자 정보를 불러오는 중...</p>
        </div>
      </div>
    );
  }

  // 사용자 정보가 없을 때
  if (!userInfo.userId) {
    return (
      <div style={{
        minHeight: '100vh',
        backgroundColor: '#f8f9fa',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center'
      }}>
        <div style={{
          textAlign: 'center',
          color: '#666'
        }}>
          <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>🚫</div>
          <p>로그인이 필요합니다.</p>
          <button
            onClick={() => navigate('/login')}
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
            로그인하기
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
          <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
            <span style={{ color: '#666', fontSize: '0.9rem' }}>
              {userInfo.userName}님 환영합니다
            </span>
            <nav style={{ display: 'flex', gap: '1rem' }}>
              <button
                onClick={() => navigate('/author-mypage')}
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
                작가 페이지
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
          도서 등록
        </h2>

        {/* 탭 메뉴 */}
        <div style={{
          display: 'flex',
          marginBottom: '2rem',
          borderBottom: '1px solid #ddd'
        }}>
          <button
            onClick={() => setActiveTab('new')}
            style={{
              padding: '1rem 2rem',
              backgroundColor: 'transparent',
              border: 'none',
              borderBottom: activeTab === 'new' ? '3px solid #28a745' : '3px solid transparent',
              color: activeTab === 'new' ? '#28a745' : '#666',
              fontWeight: activeTab === 'new' ? 'bold' : 'normal',
              cursor: 'pointer',
              fontSize: '1rem'
            }}
          >
            새 도서 작성
          </button>
          <button
            onClick={() => setActiveTab('drafts')}
            style={{
              padding: '1rem 2rem',
              backgroundColor: 'transparent',
              border: 'none',
              borderBottom: activeTab === 'drafts' ? '3px solid #28a745' : '3px solid transparent',
              color: activeTab === 'drafts' ? '#28a745' : '#666',
              fontWeight: activeTab === 'drafts' ? 'bold' : 'normal',
              cursor: 'pointer',
              fontSize: '1rem'
            }}
          >
            임시 저장된 원고 ({savedDrafts.length})
          </button>
        </div>

        {/* 새 도서 작성 탭 */}
        {activeTab === 'new' && (
          <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '2rem' }}>
            {/* 왼쪽: 입력 폼 */}
            <div style={{
              backgroundColor: '#fff',
              borderRadius: '8px',
              padding: '2rem',
              boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
            }}>
              <h3 style={{
                fontSize: '1.5rem',
                fontWeight: 'bold',
                color: '#333',
                marginBottom: '1.5rem'
              }}>
                원고 작성
              </h3>

              {/* 필명 입력 */}
              <div style={{ marginBottom: '1.5rem' }}>
                <label style={{
                  display: 'block',
                  marginBottom: '0.5rem',
                  color: '#333',
                  fontWeight: '500'
                }}>
                  필명 *
                </label>
                <input
                  type="text"
                  name="penName"
                  value={bookInfo.penName}
                  onChange={handleInputChange}
                  placeholder="작가 필명을 입력하세요"
                  style={{
                    width: '100%',
                    padding: '0.8rem',
                    border: errors.penName ? '2px solid #dc3545' : '1px solid #ddd',
                    borderRadius: '4px',
                    fontSize: '1rem',
                    outline: 'none',
                    boxSizing: 'border-box'
                  }}
                />
                {errors.penName && (
                  <span style={{ color: '#dc3545', fontSize: '0.8rem', marginTop: '0.3rem', display: 'block' }}>
                    {errors.penName}
                  </span>
                )}
              </div>

              {/* 제목 입력 */}
              <div style={{ marginBottom: '1.5rem' }}>
                <label style={{
                  display: 'block',
                  marginBottom: '0.5rem',
                  color: '#333',
                  fontWeight: '500'
                }}>
                  도서 제목 *
                </label>
                <input
                  type="text"
                  name="title"
                  value={bookInfo.title}
                  onChange={handleInputChange}
                  placeholder="도서 제목을 입력하세요"
                  style={{
                    width: '100%',
                    padding: '0.8rem',
                    border: errors.title ? '2px solid #dc3545' : '1px solid #ddd',
                    borderRadius: '4px',
                    fontSize: '1rem',
                    outline: 'none',
                    boxSizing: 'border-box'
                  }}
                />
                {errors.title && (
                  <span style={{ color: '#dc3545', fontSize: '0.8rem', marginTop: '0.3rem', display: 'block' }}>
                    {errors.title}
                  </span>
                )}
              </div>

              {/* 내용 입력 */}
              <div style={{ marginBottom: '1.5rem' }}>
                <label style={{
                  display: 'block',
                  marginBottom: '0.5rem',
                  color: '#333',
                  fontWeight: '500'
                }}>
                  도서 내용 *
                </label>
                <textarea
                  name="content"
                  value={bookInfo.content}
                  onChange={handleInputChange}
                  placeholder="도서 내용을 입력하세요"
                  rows={18}
                  style={{
                    width: '100%',
                    padding: '0.8rem',
                    border: errors.content ? '2px solid #dc3545' : '1px solid #ddd',
                    borderRadius: '4px',
                    fontSize: '1rem',
                    outline: 'none',
                    boxSizing: 'border-box',
                    resize: 'vertical',
                    fontFamily: 'inherit'
                  }}
                />
                {errors.content && (
                  <span style={{ color: '#dc3545', fontSize: '0.8rem', marginTop: '0.3rem', display: 'block' }}>
                    {errors.content}
                  </span>
                )}
                <div style={{ 
                  textAlign: 'right', 
                  color: '#666', 
                  fontSize: '0.8rem', 
                  marginTop: '0.5rem' 
                }}>
                  {bookInfo.content.length}자
                </div>
              </div>

              {/* 버튼들 */}
              <div style={{ display: 'flex', gap: '1rem' }}>
                <button
                  onClick={() => setShowSaveModal(true)}
                  disabled={!bookInfo.title.trim() && !bookInfo.content.trim()}
                  style={{
                    padding: '0.8rem 1.5rem',
                    backgroundColor: '#6c757d',
                    color: '#fff',
                    border: 'none',
                    borderRadius: '4px',
                    cursor: (!bookInfo.title.trim() && !bookInfo.content.trim()) ? 'not-allowed' : 'pointer',
                    fontSize: '1rem',
                    opacity: (!bookInfo.title.trim() && !bookInfo.content.trim()) ? 0.6 : 1
                  }}
                >
                  임시 저장
                </button>
                <button
                  onClick={generateAIContent}
                  disabled={isGenerating || (!bookInfo.title.trim() || !bookInfo.content.trim() || !bookInfo.penName.trim())}
                  style={{
                    flex: 1,
                    padding: '0.8rem 1.5rem',
                    backgroundColor: isGenerating ? '#6c757d' : '#007bff',
                    color: '#fff',
                    border: 'none',
                    borderRadius: '4px',
                    cursor: isGenerating || (!bookInfo.title.trim() || !bookInfo.content.trim() || !bookInfo.penName.trim()) ? 'not-allowed' : 'pointer',
                    fontSize: '1rem',
                    fontWeight: '500'
                  }}
                >
                  {isGenerating ? '🤖 AI 분석 중...' : '🤖 AI 분석 시작'}
                </button>
              </div>
            </div>

            {/* 오른쪽: AI 생성 결과 */}
            <div style={{
              backgroundColor: '#fff',
              borderRadius: '8px',
              padding: '2rem',
              boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
            }}>
              <h3 style={{
                fontSize: '1.5rem',
                fontWeight: 'bold',
                color: '#333',
                marginBottom: '1.5rem'
              }}>
                AI 분석 결과
              </h3>

              {!hasGenerated && !isGenerating && (
                <div style={{
                  textAlign: 'center',
                  padding: '3rem 0',
                  color: '#666'
                }}>
                  <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>🤖</div>
                  <p>필명, 제목, 내용을 입력한 후<br />AI 분석을 시작해주세요</p>
                </div>
              )}

              {isGenerating && (
                <div style={{
                  textAlign: 'center',
                  padding: '3rem 0',
                  color: '#666'
                }}>
                  <div style={{ fontSize: '3rem', marginBottom: '1rem' }}>⏳</div>
                  <p>AI가 도서를 분석하고 있습니다...</p>
                  <div style={{
                    width: '100%',
                    height: '4px',
                    backgroundColor: '#f0f0f0',
                    borderRadius: '2px',
                    marginTop: '1rem',
                    overflow: 'hidden'
                  }}>
                    <div style={{
                      width: '100%',
                      height: '100%',
                      backgroundColor: '#007bff',
                      animation: 'loading 2s ease-in-out infinite'
                    }} />
                  </div>
                </div>
              )}

              {hasGenerated && (
                <div>
                  {/* 작가명 표시 */}
                  <div style={{ marginBottom: '1.5rem' }}>
                    <h4 style={{ color: '#333', marginBottom: '0.5rem' }}>작가명</h4>
                    <div style={{
                      padding: '0.5rem 1rem',
                      backgroundColor: '#f8f9fa',
                      color: '#333',
                      borderRadius: '4px',
                      border: '1px solid #ddd',
                      fontSize: '1rem',
                      fontWeight: '500'
                    }}>
                      {bookInfo.penName}
                    </div>
                  </div>

                  {/* 표지 이미지 */}
                  <div style={{ marginBottom: '1.5rem' }}>
                    <h4 style={{ color: '#333', marginBottom: '0.5rem' }}>AI 생성 표지</h4>
                    <div style={{
                      width: '100%',
                      height: '200px',
                      backgroundColor: '#f8f9fa',
                      borderRadius: '4px',
                      backgroundImage: `url(${bookInfo.cover})`,
                      backgroundSize: 'cover',
                      backgroundPosition: 'center',
                      border: '1px solid #ddd'
                    }} />
                  </div>

                  {/* 카테고리 */}
                  <div style={{ marginBottom: '1.5rem' }}>
                    <h4 style={{ color: '#333', marginBottom: '0.5rem' }}>추천 카테고리</h4>
                    <div style={{
                      padding: '0.5rem 1rem',
                      backgroundColor: '#e8f5e8',
                      color: '#28a745',
                      borderRadius: '20px',
                      display: 'inline-block',
                      fontSize: '0.9rem',
                      fontWeight: '500'
                    }}>
                      {bookInfo.category}
                    </div>
                  </div>

                  {/* 추천 가격 */}
                  <div style={{ marginBottom: '1.5rem' }}>
                    <h4 style={{ color: '#333', marginBottom: '0.5rem' }}>AI 추천 가격</h4>
                    <div style={{
                      padding: '1rem',
                      backgroundColor: '#f8f9fa',
                      borderRadius: '4px',
                      border: '1px solid #ddd'
                    }}>
                      <div style={{
                        fontSize: '1.2rem',
                        fontWeight: 'bold',
                        color: '#007bff',
                        marginBottom: '0.5rem'
                      }}>
                        {bookInfo.suggestedPrice?.toLocaleString()}P
                      </div>
                      <div style={{ fontSize: '0.8rem', color: '#666' }}>
                        내용 분석 결과 책정된 최종 가격입니다 
                      </div>
                    </div>
                  </div>

                  {/* AI 생성 설명 */}
                  <div style={{ marginBottom: '1.5rem' }}>
                    <h4 style={{ color: '#333', marginBottom: '0.5rem' }}>AI 생성 설명</h4>
                    <textarea
                      name="description"
                      value={bookInfo.description}
                      onChange={handleInputChange}
                      rows={4}
                      style={{
                        width: '100%',
                        padding: '0.8rem',
                        border: '1px solid #ddd',
                        borderRadius: '4px',
                        fontSize: '0.9rem',
                        outline: 'none',
                        boxSizing: 'border-box',
                        resize: 'vertical'
                      }}
                    />
                  </div>

                  {/* 최종 등록 버튼 */}
                  <button
                    onClick={submitBook}
                    style={{
                      width: '100%',
                      padding: '1rem',
                      backgroundColor: '#28a745',
                      color: '#fff',
                      border: 'none',
                      borderRadius: '4px',
                      cursor: 'pointer',
                      fontSize: '1rem',
                      fontWeight: 'bold'
                    }}
                  >
                    📚 최종 도서 등록 요청
                  </button>
                </div>
              )}
            </div>
          </div>
        )}

        {/* 임시 저장된 원고 탭 */}
        {activeTab === 'drafts' && (
          <div style={{
            backgroundColor: '#fff',
            borderRadius: '8px',
            padding: '2rem',
            boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
          }}>
            <h3 style={{
              fontSize: '1.5rem',
              fontWeight: 'bold',
              color: '#333',
              marginBottom: '1.5rem'
            }}>
              임시 저장된 원고 목록
            </h3>

            {savedDrafts.length > 0 ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                {savedDrafts.map(draft => (
                  <div key={draft.id} style={{
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
                          {draft.title}
                        </h4>
                        <p style={{
                          color: '#666',
                          marginBottom: '0.5rem',
                          fontSize: '0.9rem',
                          lineHeight: '1.4'
                        }}>
                          {draft.content.substring(0, 150)}
                          {draft.content.length > 150 ? '...' : ''}
                        </p>
                        <div style={{
                          display: 'flex',
                          gap: '1rem',
                          fontSize: '0.8rem',
                          color: '#999'
                        }}>
                          <span>저장일: {new Date(draft.saveDate).toLocaleDateString()}</span>
                          <span>글자 수: {draft.wordCount.toLocaleString()}자</span>
                        </div>
                      </div>
                      <div style={{
                        display: 'flex',
                        flexDirection: 'column',
                        gap: '0.5rem'
                      }}>
                        <button
                          onClick={() => loadDraft(draft)}
                          style={{
                            padding: '0.5rem 1rem',
                            backgroundColor: '#28a745',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '4px',
                            cursor: 'pointer',
                            fontSize: '0.9rem'
                          }}
                        >
                          불러오기
                        </button>
                        <button
                          onClick={() => openDeleteModal(draft)}
                          style={{
                            padding: '0.5rem 1rem',
                            backgroundColor: '#dc3545',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '4px',
                            cursor: 'pointer',
                            fontSize: '0.9rem'
                          }}
                        >
                          삭제
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
                <p>임시 저장된 원고가 없습니다.</p>
                <button
                  onClick={() => setActiveTab('new')}
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
                  새 원고 작성하기
                </button>
              </div>
            )}
          </div>
        )}
      </div>

      {/* 임시 저장 모달 */}
      {showSaveModal && (
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
            <h3 style={{ marginBottom: '1.5rem', color: '#333' }}>임시 저장</h3>
            
            <div style={{ marginBottom: '1.5rem' }}>
              <label style={{
                display: 'block',
                marginBottom: '0.5rem',
                color: '#333',
                fontWeight: '500'
              }}>
                저장할 제목
              </label>
              <input
                type="text"
                value={draftTitle}
                onChange={(e) => setDraftTitle(e.target.value)}
                placeholder={bookInfo.title || '제목을 입력하세요'}
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

            <div style={{ display: 'flex', gap: '1rem' }}>
              <button
                onClick={() => {
                  setShowSaveModal(false);
                  setDraftTitle('');
                }}
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
                onClick={saveDraft}
                style={{
                  flex: 1,
                  padding: '0.8rem',
                  backgroundColor: '#28a745',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer'
                }}
              >
                저장
              </button>
            </div>
          </div>
        </div>
      )}

      {/* 삭제 확인 모달 */}
      {showDeleteModal && draftToDelete && (
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
            <h3 style={{ 
              marginBottom: '1rem', 
              color: '#333',
              fontSize: '1.3rem'
            }}>
              원고 삭제 확인
            </h3>
            
            <p style={{ 
              color: '#666', 
              marginBottom: '1.5rem',
              lineHeight: '1.5'
            }}>
              <strong>"{draftToDelete.title}"</strong> 원고를 정말로 삭제하시겠습니까?
              <br />
              <span style={{ fontSize: '0.9rem', color: '#999' }}>
                삭제된 원고는 복구할 수 없습니다.
              </span>
            </p>

            <div style={{ display: 'flex', gap: '1rem' }}>
              <button
                onClick={() => {
                  setShowDeleteModal(false);
                  setDraftToDelete(null);
                }}
                style={{
                  flex: 1,
                  padding: '0.8rem',
                  backgroundColor: '#6c757d',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer',
                  fontSize: '1rem'
                }}
              >
                취소
              </button>
              <button
                onClick={() => deleteDraft(draftToDelete.id)}
                style={{
                  flex: 1,
                  padding: '0.8rem',
                  backgroundColor: '#dc3545',
                  color: '#fff',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer',
                  fontSize: '1rem',
                  fontWeight: '500'
                }}
              >
                삭제
              </button>
            </div>
          </div>
        </div>
      )}

      {/* CSS 애니메이션 */}
      <style>{`
        @keyframes loading {
          0% { transform: translateX(-100%); }
          50% { transform: translateX(0%); }
          100% { transform: translateX(100%); }
        }
      `}</style>
    </div>
  );
};

export default BookRegisterPage;