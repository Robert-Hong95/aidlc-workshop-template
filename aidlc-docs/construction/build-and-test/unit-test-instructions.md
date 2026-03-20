# Unit Test Execution

## TDD 사용 여부
모든 4개 Unit에서 TDD 방식으로 코드 생성 완료. 단위 테스트는 Code Generation 단계에서 이미 실행 및 통과됨.

## 테스트 실행

### 전체 테스트
```bash
cd table-order/backend
./gradlew test
```

### Unit별 테스트
```bash
# Unit 1 - Auth
./gradlew test --tests "com.tableorder.auth.*"
./gradlew test --tests "com.tableorder.common.config.*"

# Unit 2 - Store/Table
./gradlew test --tests "com.tableorder.store.*"
./gradlew test --tests "com.tableorder.table.*"

# Unit 3 - Menu
./gradlew test --tests "com.tableorder.menu.*"

# Unit 4 - Order+SSE
./gradlew test --tests "com.tableorder.order.*"
```

## 테스트 결과 (83 tests, 0 failures)

| Package | Class | Tests |
|---------|-------|-------|
| auth.domain | AdminTest | 5 |
| auth.service | AuthServiceTest (LoginAdmin) | 7 |
| auth.service | AuthServiceTest (LoginTable) | 3 |
| auth.service | AuthServiceTest (LoginByQrToken) | 3 |
| auth.service | AuthServiceTest (GenerateQrToken) | 1 |
| auth.service | AuthServiceTest (RefreshToken) | 3 |
| common.config | JwtTokenProviderTest | 5 |
| common.config | RateLimitInterceptorTest | 2 |
| store.service | StoreServiceTest | 6 |
| table.domain | TableSessionTest | 2 |
| table.service | TableServiceTest | 11 |
| menu.service | MenuServiceTest | 13 |
| menu.service | FileStorageServiceTest | 4 |
| order.domain | OrderTest | 4 |
| order.service | OrderServiceTest | 10 |
| order.service | SseEmitterServiceTest | 3 |
| - | TableOrderApplicationTests | 1 |
| **Total** | | **83** |

## 테스트 리포트
- HTML: `build/reports/tests/test/index.html`
- XML: `build/test-results/test/`
