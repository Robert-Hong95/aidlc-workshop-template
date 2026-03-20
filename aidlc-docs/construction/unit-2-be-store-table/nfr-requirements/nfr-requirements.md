# Unit 2-BE Store/Table API - NFR Requirements

## 공통 NFR (Unit 1-BE에서 결정, 그대로 적용)
- JWT: Access Token 1hr, Refresh Token 16hr (HttpOnly Cookie), QR Token 10min
- CORS: localhost:3000, localhost:3001, credentials=true
- 비밀번호 정책: 최소 8자, 영문+숫자+특수문자
- 로깅: 실패 시 WARN 레벨, SLF4J
- API 응답 시간: < 1초

## Unit 2-BE 특화 NFR

### NFR-ST-01: Rate Limiting
- 매장 등록 API (POST /api/stores): IP 기반 분당 10회 제한
- 구현: Spring 인터셉터 + 인메모리 카운터 (ConcurrentHashMap, 1분 윈도우)

### NFR-ST-02: endSession 트랜잭션 전략
- 배치 처리: 100건씩 나눠서 주문 직렬화+삭제
- 각 배치는 별도 트랜잭션 (부분 실패 시 재시도 가능)
- 전체 세션 종료는 모든 배치 완료 후 수행

### NFR-ST-03: 데이터 보관
- order_history: 1년 보관 후 자동 삭제
- 삭제 방식: 스케줄러 (향후 구현, MVP에서는 수동)

### NFR-ST-04: 입력 검증
- store_code: 빈 값만 아니면 허용 (@NotBlank)
- table_no: @NotNull, 양수
- password: 비밀번호 정책 적용 (영문+숫자+특수문자, 8자 이상)

### NFR-ST-05: 조회 성능
- 테이블 목록+활성 세션: LEFT JOIN 단일 쿼리
- Cursor 기반 페이지네이션: WHERE id < :lastId ORDER BY id DESC LIMIT :size+1
