# Business Rules - Unit 2-BE (Store/Table)

## 1. 매장 규칙

### BR-STORE-01: 매장 코드 유일성
- storeCode는 시스템 전체에서 유일해야 함
- 중복 시 DUPLICATE_STORE_CODE (409)

### BR-STORE-02: 매장 필수 필드
- storeCode: @NotBlank
- name: @NotBlank

### BR-STORE-03: 매장 삭제
- 매장 삭제 시 연관 데이터(테이블, 메뉴, 주문 등) 존재 여부 확인 없이 삭제 (MVP - cascade는 DB 레벨)

## 2. 테이블 규칙

### BR-TABLE-01: 테이블 번호 유일성
- 동일 매장 내 tableNo 유일
- 중복 시 DUPLICATE_TABLE_NO (409)

### BR-TABLE-02: 테이블 설정 필수 필드
- tableNo: @NotNull @Min(1)
- password: @NotBlank @Size(min=4)

### BR-TABLE-03: 비밀번호 저장
- BCryptPasswordEncoder로 해싱 후 저장

## 3. 세션 규칙

### BR-SESSION-01: 활성 세션
- 테이블당 활성 세션(endedAt IS NULL)은 최대 1개
- 활성 세션 조회: findByTableIdAndEndedAtIsNull

### BR-SESSION-02: 세션 자동 시작
- 첫 주문 생성 시 활성 세션이 없으면 자동 시작 (Unit 4에서 구현)

### BR-SESSION-03: 세션 종료 (이용 완료)
- 활성 세션이 없으면 SESSION_NOT_FOUND
- 세션의 모든 주문을 JSON 직렬화 → order_history에 저장
- orders, order_items에서 해당 세션 주문 삭제
- session.endedAt = now()

## 4. DTO 정의

### Request DTOs
| DTO | 필드 | 검증 |
|-----|------|------|
| StoreCreateRequest | storeCode, name | 모두 @NotBlank |
| StoreUpdateRequest | name | @NotBlank |
| TableSetupRequest | tableNo, password | tableNo @NotNull @Min(1), password @NotBlank @Size(min=4) |

### Response DTOs
| DTO | 필드 |
|-----|------|
| StoreResponse | id, storeCode, name, createdAt |
| TableResponse | id, storeId, tableNo, hasActiveSession, createdAt |
| OrderHistoryResponse | id, orderData, totalAmount, orderedAt, completedAt |
