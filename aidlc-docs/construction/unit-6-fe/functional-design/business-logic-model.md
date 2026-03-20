# Business Logic Model - Unit 6-FE (Store/Table 관리 UI)

## 1. 매장 관리 (Store Management)

### 매장 목록 조회
1. 관리자 인증 확인
2. API 호출: GET /api/admin/stores
3. 매장 목록 표시 (매장명, 매장코드, 생성일)

### 매장 등록
1. 매장 등록 폼 표시 (매장명, 주소, 전화번호)
2. 클라이언트 검증 (필수 필드)
3. API 호출: POST /api/admin/stores
4. 성공 시 목록 갱신 + 토스트 알림

### 매장 수정
1. 매장 선택 → 수정 폼 표시 (기존 데이터 프리필)
2. 클라이언트 검증
3. API 호출: PUT /api/admin/stores/{id}
4. 성공 시 목록 갱신 + 토스트 알림

### 매장 전환
1. AdminHeader 매장명 클릭 → 드롭다운 표시
2. 매장 선택 시 auth-store의 storeId/storeName 업데이트
3. 현재 페이지 데이터 리페치 (React Query invalidation)

## 2. 테이블 관리 (Table Management)

### 테이블 목록 조회
1. 현재 매장의 테이블 목록 조회: GET /api/admin/tables?storeId={storeId}
2. 리스트/카드 토글 뷰로 표시
3. 각 테이블: 번호, 세션 상태(활성/비활성), 총 주문액

### 테이블 추가
1. 테이블 설정 폼: 테이블번호 + 4자리 숫자 PIN
2. 클라이언트 검증 (번호 중복, PIN 4자리)
3. API 호출: POST /api/admin/tables
4. 성공 시 목록 갱신

### 테이블 세션 — 이용 완료
1. "이용 완료" 버튼 클릭
2. 확인 팝업 표시: 총 주문 금액 요약 + 확인/취소
3. 확인 시 API 호출: POST /api/admin/tables/{id}/complete-session
4. 성공 시 세션 종료, 테이블 상태 리셋

### 주문 삭제
1. 테이블 상세에서 주문 삭제 버튼 클릭
2. ConfirmDialog 표시
3. 확인 시 API 호출: DELETE /api/admin/orders/{orderId}
4. 성공 시 주문 목록 갱신 + 총 금액 재계산

### 과거 주문 내역 조회
1. "과거 내역" 버튼 클릭 → 모달 표시
2. 날짜 필터: 프리셋 버튼 (오늘/7일/30일) + 커스텀 범위
3. API 호출: GET /api/admin/tables/{id}/history?from={}&to={}
4. 시간 역순 정렬, 주문번호/시각/메뉴/총금액/이용완료시각 표시
