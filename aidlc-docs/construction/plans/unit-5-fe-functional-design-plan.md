# Functional Design Plan - Unit 5-FE (공통 레이아웃 + Auth UI)

## Unit 개요
- **Unit**: Unit 5-FE
- **내용**: 공통 레이아웃 + Auth UI
- **Stories**: US-A01 (매장 인증), US-C01 (테이블 자동 로그인)
- **작업 디렉토리**: `table-order/frontend/`

## 범위
1. 공유 패키지 기본 구현 (`packages/ui`, `packages/api-client`, `packages/shared`)
2. 관리자앱 로그인 UI (LoginPage, useAdminAuth, auth-store)
3. 고객앱 테이블 초기 설정 및 자동 로그인 UI (SetupPage, useTableAuth, auth-store)
4. 공통 레이아웃 컴포넌트 (CustomerHeader, BottomNav, AdminHeader, Sidebar, AdminLayout)

---

## 설계 계획

- [x] Step 1: 공유 패키지 구조 설계 (packages/ui, api-client, shared)
- [x] Step 2: API Client 인증 모듈 설계 (auth.ts, client.ts 인터셉터)
- [x] Step 3: 공유 UI 컴포넌트 설계 (Button, Card, Input, Modal, Toast, Spinner, EmptyState)
- [x] Step 4: 관리자앱 인증 플로우 설계 (LoginPage, auth-store, useAdminAuth)
- [x] Step 5: 고객앱 인증 플로우 설계 (SetupPage, auth-store, useTableAuth, 자동 로그인)
- [x] Step 6: 관리자앱 공통 레이아웃 설계 (AdminHeader, Sidebar, AdminLayout)
- [x] Step 7: 고객앱 공통 레이아웃 설계 (CustomerHeader, BottomNav, CartFloatingButton)
- [x] Step 8: Functional Design 산출물 생성

---

## 명확화 질문

아래 질문에 답변해 주세요. 각 질문의 [Answer]: 태그 뒤에 선택지 알파벳을 입력해 주세요.

### Question 1
관리자앱 사이드바 네비게이션 스타일은 어떤 형태를 선호하시나요?

A) 항상 펼쳐진 고정 사이드바 (데스크톱 전용)
B) 접기/펼치기 가능한 사이드바 (아이콘 모드 ↔ 전체 모드)
C) 모바일 반응형 (데스크톱: 고정 사이드바, 모바일: 햄버거 메뉴 드로어)
D) Other (please describe after [Answer]: tag below)

[Answer]: D. C + 데스크톱 사이드바 토글 가능하도록 해줘.

### Question 2
고객앱 하단 네비게이션(BottomNav)에 표시할 탭 구성은 어떻게 하시겠습니까?

A) 3탭: 메뉴 | 장바구니 | 주문내역
B) 2탭: 메뉴 | 주문내역 (장바구니는 플로팅 버튼으로만)
C) Other (please describe after [Answer]: tag below)

[Answer]: A

### Question 3
관리자 로그인 실패 시 로그인 시도 제한 정책은 어떻게 하시겠습니까?

A) 5회 실패 시 30초 대기 (프론트엔드에서 타이머 표시)
B) 5회 실패 시 계정 잠금 (관리자 수동 해제 필요)
C) 시도 제한 없이 에러 메시지만 표시 (MVP 단순화)
D) Other (please describe after [Answer]: tag below)

[Answer]: D. 애플 정책 따라줘.

### Question 4
고객앱 테이블 초기 설정 후 자동 로그인 정보 저장 방식은?

A) localStorage에 토큰 + 매장/테이블 정보 저장
B) localStorage에 매장/테이블/비밀번호 저장 후 매번 로그인 API 호출
C) Other (please describe after [Answer]: tag below)

[Answer]: C. 자동 로그인 정보 저장방식은 어떻게 설계하는게 좋을지 제안해줘.

### Question 5
고객앱 색상 테마는 어떤 느낌을 선호하시나요?

A) 따뜻한 톤 (오렌지/레드 계열 - 음식점 느낌)
B) 깔끔한 톤 (블루/그린 계열 - 모던 느낌)
C) 중립 톤 (그레이/화이트 - 미니멀)
D) Other (please describe after [Answer]: tag below)

[Answer]: D. AWS 느낌 비슷하게 해줘.

### Question 6
관리자앱 색상 테마는 어떤 느낌을 선호하시나요?

A) 다크 사이드바 + 라이트 콘텐츠 (전문적인 느낌)
B) 전체 라이트 테마 (밝고 깔끔)
C) 전체 다크 테마
D) Other (please describe after [Answer]: tag below)

[Answer]: A

### Question 7
고객앱(태블릿)에서 정상 사용 중 설정 화면으로 재진입하는 경로가 필요합니다. (테이블 변경, 매장 변경, 인증 정보 재설정 등) 어떤 방식을 선호하시나요?

A) Header에 작은 설정(⚙️) 아이콘 → 테이블 비밀번호 입력 후 설정 화면 진입 (고객 실수 방지)
B) Header에 설정(⚙️) 아이콘 → 비밀번호 확인 없이 바로 설정 화면 진입
C) 설정 재진입 경로 불필요 (자동 로그인 실패 시에만 설정 화면 표시, 수동 재진입 없음)
D) Other (please describe after [Answer]: tag below)

[Answer]: A
