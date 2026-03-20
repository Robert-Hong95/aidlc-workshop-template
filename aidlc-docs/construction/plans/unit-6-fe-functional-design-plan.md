# Functional Design Plan - Unit 6-FE (Store/Table 관리 UI)

## Unit Context
- **Stories**: US-A05 (매장 관리), US-A03 (테이블 관리)
- **작업 디렉토리**: table-order/frontend/apps/admin/
- **관련 Backend API**: AdminStoreController, AdminTableController (미완성 — 타입 기반 개발)

## Plan Steps
- [x] Step 1: 비즈니스 로직 모델 설계
- [x] Step 2: 비즈니스 규칙 정의
- [x] Step 3: 도메인 엔티티 정의
- [x] Step 4: Frontend 컴포넌트 설계

---

## Questions

### Q1. 매장 관리 범위
매장 관리 화면에서 관리자가 할 수 있는 작업 범위는?

A) 매장 정보 조회만 (등록은 별도 프로세스)
B) 매장 정보 조회 + 수정 (이름, 주소 등)
C) 매장 등록 + 조회 + 수정 (CRUD 전체)

[Answer]: C

### Q2. 테이블 관리 화면 구성
테이블 관리 화면에서 테이블 목록을 어떤 형태로 표시할까요?

A) 리스트 형태 (테이블 행)
B) 카드 그리드 형태 (대시보드와 유사)
C) 리스트 + 카드 토글 (사용자 선택)

[Answer]:C

### Q3. 테이블 세션 관리 — "이용 완료" 동작
테이블 "이용 완료" 시 추가 확인 절차는?

A) 확인 팝업 1단계 ("이용 완료 처리하시겠습니까?")
B) 2단계 확인 (1차 팝업 + 비밀번호 재입력)
C) 확인 팝업 + 총 주문 금액 요약 표시 후 확인

[Answer]: C

### Q4. 과거 주문 내역 모달 — 날짜 필터
과거 주문 내역의 날짜 필터 UI는?

A) 단일 날짜 선택 (DatePicker)
B) 시작일~종료일 범위 선택 (DateRangePicker)
C) 프리셋 버튼 (오늘, 최근 7일, 최근 30일) + 커스텀 범위

[Answer]: C

### Q5. 테이블 설정 — 비밀번호 정책
테이블 비밀번호 설정 시 제약 조건은?

A) 4자리 숫자 PIN
B) 4~8자리 영숫자
C) 제한 없음 (자유 입력)

[Answer]: A

### Q6. 매장 전환 UI
다중 매장 전환은 어디에서 할 수 있나요?

A) AdminHeader의 매장명 클릭 → 드롭다운 선택
B) 매장 관리 페이지에서만 전환
C) Sidebar 상단에 매장 선택 드롭다운

[Answer]: A
