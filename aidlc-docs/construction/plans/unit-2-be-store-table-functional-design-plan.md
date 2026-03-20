# Unit 2-BE Store/Table API - Functional Design Plan

## Unit 정보
- **Unit**: Unit 2-BE (Store/Table API)
- **Stories**: US-A05 (매장 관리), US-A03 (테이블 관리)
- **범위**: Backend only (table-order/backend/)

## 설계 계획

- [x] Step 1: 질문 수집 및 분석
- [x] Step 2: Domain Entity 상세 설계
- [x] Step 3: Business Logic 상세 설계
- [x] Step 4: Business Rules 정의
- [x] Step 5: DTO/API 엔드포인트 설계
- [x] Step 6: 산출물 파일 생성

## 확정된 설계 결정
- Q1=A: 매장+관리자 단일 API 트랜잭션 동시 생성
- Q2=A: 현재 스키마 유지 (store_code, name)
- Q3=B: 고객 첫 주문 시 세션 자동 시작
- Q4=A: JSON 직렬화 → order_history 저장 후 orders 삭제
- Q5=B: Cursor 기반 페이지네이션
- Q6=B: N:1 관리자-매장 관계
