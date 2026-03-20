# Business Logic Model - Unit 5-FE (공통 레이아웃 + Auth UI)

## 1. 관리자 인증 플로우 (US-A01)

### 1.1 로그인 플로우
```
LoginPage 진입
  → 매장코드, 사용자명, 비밀번호 입력
  → 프론트엔드 폼 검증 (빈 필드 체크)
  → 로그인 시도 제한 체크 (점진적 지연 정책)
    → 제한 중이면: 남은 대기 시간 타이머 표시, 제출 버튼 비활성화
    → 제한 아니면: POST /api/admin/auth/login 호출
      → 성공: accessToken + refreshToken 저장 → 대시보드로 이동, 실패 카운트 리셋
      → 실패: 에러 메시지 표시, 실패 카운트 증가, 점진적 지연 적용
```

### 1.2 점진적 로그인 시도 제한 (Apple 스타일)
| 실패 횟수 | 대기 시간 |
|-----------|----------|
| 1~5회 | 즉시 재시도 |
| 6회 | 1분 |
| 7회 | 5분 |
| 8회 | 15분 |
| 9회 | 60분 |
| 10회+ | 60분 |

- 실패 횟수와 마지막 실패 시각을 localStorage에 저장
- 로그인 성공 시 실패 카운트 리셋
- 대기 중 카운트다운 타이머 UI 표시

### 1.3 세션 관리
```
앱 시작 (브라우저 로드/새로고침)
  → localStorage에서 accessToken 확인
    → 없음: 로그인 페이지로 이동
    → 있음: 토큰 유효성 확인 (만료 시각 체크)
      → 유효: 대시보드로 이동
      → 만료: refreshToken으로 갱신 시도
        → 갱신 성공: 새 accessToken 저장, 대시보드로 이동
        → 갱신 실패: 토큰 삭제, 로그인 페이지로 이동
```

### 1.4 로그아웃
```
로그아웃 버튼 클릭
  → localStorage에서 토큰 삭제
  → auth-store 초기화
  → 로그인 페이지로 이동
```

---

## 2. 고객 테이블 인증 플로우 (US-C01)

### 2.1 초기 설정 플로우
```
SetupPage 진입 (저장된 인증 정보 없을 때)
  → 매장코드, 테이블번호, 비밀번호 입력
  → 프론트엔드 폼 검증
  → POST /api/customer/auth/login 호출
    → 성공: accessToken + refreshToken + 매장코드/테이블번호 저장 → 메뉴 페이지로 이동
    → 실패: 에러 메시지 표시
```

### 2.2 자동 로그인 플로우 (Refresh Token 방식)
```
앱 시작 (브라우저 로드/새로고침)
  → localStorage에서 accessToken 확인
    → 없음: refreshToken 확인
      → 없음: SetupPage로 이동
      → 있음: refreshToken으로 갱신 시도
        → 성공: 새 accessToken 저장, 메뉴 페이지로 이동
        → 실패: 토큰 삭제, SetupPage로 이동
    → 있음: 토큰 만료 시각 체크
      → 유효: 메뉴 페이지로 이동
      → 만료: refreshToken으로 갱신 (위와 동일)
```

### 2.3 설정 재진입 플로우 (Q7)
```
CustomerHeader의 설정(⚙️) 아이콘 클릭
  → 비밀번호 확인 모달 표시
    → 비밀번호 입력 → 서버 검증 (POST /api/customer/auth/verify-password)
      → 성공: SetupPage로 이동 (기존 토큰 삭제)
      → 실패: 에러 메시지 표시
```

---

## 3. API Client 인터셉터 로직

### 3.1 요청 인터셉터
```
모든 API 요청 전
  → auth-store에서 accessToken 조회
    → 있음: Authorization: Bearer {token} 헤더 추가
    → 없음: 헤더 없이 요청 (공개 API)
```

### 3.2 응답 인터셉터
```
API 응답 수신
  → 401 Unauthorized 응답
    → refreshToken으로 토큰 갱신 시도
      → 성공: 원래 요청 재시도
      → 실패: 토큰 삭제, 로그인/설정 페이지로 이동
  → 기타 에러: 에러 응답 반환
```

### 3.3 토큰 갱신 동시성 처리
```
여러 API가 동시에 401을 받을 때
  → 첫 번째 요청만 갱신 API 호출
  → 나머지 요청은 갱신 완료까지 대기 (Promise 큐)
  → 갱신 완료 후 대기 중인 요청 모두 재시도
```
