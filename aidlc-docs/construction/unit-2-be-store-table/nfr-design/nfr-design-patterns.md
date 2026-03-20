# Unit 2-BE Store/Table API - NFR Design Patterns

## DP-ST-01: Rate Limiting Pattern (매장 등록 API)

### 패턴: Sliding Window Counter (인메모리)
```
RateLimitInterceptor implements HandlerInterceptor
  - ConcurrentHashMap<String, Deque<Long>> requestLog
  - preHandle(): IP 추출 → 1분 윈도우 내 요청 수 확인 → 10회 초과 시 429 응답
  - 만료된 타임스탬프 자동 정리
```

### 적용 대상
- `POST /api/stores` 엔드포인트만 적용
- WebMvcConfigurer에서 해당 경로만 인터셉터 등록

## DP-ST-02: Batch Processing Pattern (세션 종료)

### 패턴: Chunk-based Batch
```
TableService.endSession(tableId)
  1. 활성 세션 조회
  2. 전체 주문 ID 목록 조회
  3. 100건씩 청크로 분할:
     - 청크 내 주문 조회
     - JSON 직렬화 → OrderHistory 벌크 저장
     - 해당 주문 벌크 삭제
  4. 모든 청크 완료 후 session.end()
```

### 트랜잭션 전략
- 전체 endSession을 하나의 @Transactional로 묶음
- 청크는 논리적 분할 (DB 부하 분산), 트랜잭션은 단일
- 실패 시 전체 롤백 (데이터 정합성 보장)

## DP-ST-03: Optimized Query Pattern (테이블+세션 조회)

### 패턴: LEFT JOIN Fetch
```sql
SELECT t.*, s.id as session_id, s.started_at
FROM store_tables t
LEFT JOIN table_sessions s ON t.id = s.table_id AND s.ended_at IS NULL
WHERE t.store_id = :storeId
```

### 구현
- JPQL 또는 @Query 어노테이션
- DTO Projection으로 직접 매핑

## DP-ST-04: Cursor Pagination Pattern (주문 내역)

### 패턴: Keyset Pagination
```sql
SELECT * FROM order_history
WHERE table_id = :tableId
  AND completed_at BETWEEN :dateFrom AND :dateTo
  AND (:lastId IS NULL OR id < :lastId)
ORDER BY id DESC
LIMIT :size + 1
```

### 구현
- size+1 조회하여 hasNext 판단
- 마지막 항목의 id를 nextLastId로 반환

## DP-ST-05: Input Validation Pattern

### 패턴: Bean Validation + Custom Validator
- @NotBlank, @NotNull: 기본 검증
- 비밀번호 정책: Unit 1에서 사용한 동일 정규식 패턴 재사용
- GlobalExceptionHandler에서 MethodArgumentNotValidException 처리 (이미 구현됨)
