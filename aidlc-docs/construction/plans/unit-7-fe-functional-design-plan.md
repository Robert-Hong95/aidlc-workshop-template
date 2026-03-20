# Functional Design Plan - Unit 7-FE (Menu UI)

## Unit Context
- **Stories**: US-A04 (메뉴 관리), US-C02 (메뉴 조회 및 탐색)
- **작업 디렉토리**: table-order/frontend/ (admin + customer)
- **관련 Backend API**: AdminMenuController, CustomerMenuController, FileController

## Plan Steps
- [ ] Step 1: 비즈니스 로직 모델 설계
- [ ] Step 2: 비즈니스 규칙 정의
- [ ] Step 3: 도메인 엔티티 정의
- [ ] Step 4: Frontend 컴포넌트 설계

---

## Questions

### Q1. 메뉴 이미지 업로드 — 미리보기
이미지 업로드 시 미리보기 방식은?

A) 업로드 전 로컬 미리보기 (FileReader)
B) 업로드 후 서버 URL로 미리보기
C) 로컬 미리보기 + 업로드 후 서버 URL 교체

[Answer]: A

### Q2. 메뉴 노출 순서 — 드래그 정렬
메뉴 순서 변경 UI는?

A) 드래그 앤 드롭 (@dnd-kit)
B) 위/아래 화살표 버튼
C) 순서 번호 직접 입력

[Answer]: A

### Q3. 카테고리 관리 범위
카테고리 관리에서 할 수 있는 작업은?

A) 카테고리 추가/삭제만
B) 카테고리 추가/수정/삭제 + 순서 변경
C) 카테고리 추가/수정/삭제 (순서 변경 없음)

[Answer]: B

### Q4. 고객 메뉴 화면 — 카테고리 탭 스타일
고객앱 카테고리 탭 UI는?

A) 상단 고정 가로 스크롤 탭
B) 좌측 세로 사이드 탭
C) 상단 고정 탭 + 스크롤 시 해당 섹션 자동 이동 (스크롤 스파이)

[Answer]: A

### Q5. 메뉴 가격 범위
메뉴 가격 유효 범위는?

A) 100원 ~ 1,000,000원
B) 0원 ~ 10,000,000원 (무료 메뉴 허용)
C) 1원 ~ 999,999원

[Answer]: A
