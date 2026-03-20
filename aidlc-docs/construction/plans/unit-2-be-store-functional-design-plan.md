# Functional Design Plan - Unit 2-BE (Store/Table API)

## 대상 Stories
- US-A05: 매장 관리 (매장 등록/조회)
- US-A03: 테이블 관리 (초기 설정, 세션 관리, 주문 삭제, 과거 내역)

## 기존 코드 (Unit 0 + Unit 1)
- Store, StoreTable 엔티티 이미 생성 (Unit 1에서 Auth 참조용)
- StoreRepository, StoreTableRepository 이미 생성
- DB 스키마: stores, store_tables, table_sessions 테이블 정의 완료

## 설계 항목
- [x] 1. Domain Entities 설계 (TableSession 엔티티 추가)
- [x] 2. Business Logic Model 설계 (매장 CRUD, 테이블 설정, 세션 라이프사이클)
- [x] 3. Business Rules 설계 (검증 규칙, 세션 전이 규칙)

---

# Functional Design 질문

## Question 1
테이블 초기 설정 시 비밀번호는 어떻게 처리하시겠습니까?

A) 관리자가 직접 비밀번호 입력 (bcrypt 해싱 저장)
B) 시스템이 자동 생성 후 관리자에게 표시
C) Other (please describe after [Answer]: tag below)

[Answer]: A (비밀번호:test1234)

## Question 2
테이블 이용 완료(세션 종료) 시 주문 데이터 이력 이동 방식은?

A) 주문 데이터를 JSON으로 직렬화하여 order_history 테이블에 저장 후 orders/order_items에서 삭제
B) orders 테이블에 status를 COMPLETED로 변경만 하고 삭제하지 않음
C) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 3
매장 관리에서 매장 수정/삭제 기능도 MVP에 포함하시겠습니까?

A) 매장 등록과 조회만 (수정/삭제 제외)
B) 매장 등록, 조회, 수정 포함 (삭제 제외)
C) 매장 CRUD 전체 포함
D) Other (please describe after [Answer]: tag below)

[Answer]: C
