# Unit Test Execution

## TDD 사용 확인
모든 Unit에서 TDD 사용. 테스트는 Code Generation 단계에서 이미 실행 및 통과됨.

## 전체 테스트 실행 (검증)
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
cd table-order/backend
./gradlew cleanTest test
```

## 예상 결과: 84 tests, 0 failures

| Test Class | Tests | Unit |
|-----------|-------|------|
| AuthServiceTest | 11 | Unit 1 |
| AdminAuthControllerTest | 4 | Unit 1 |
| TableAuthControllerTest | 2 | Unit 1 |
| StoreServiceTest | 7 | Unit 2 |
| TableServiceTest | 8 | Unit 2 |
| StoreTableControllerTest | 6 | Unit 2 |
| MenuServiceTest | 15 | Unit 3 |
| FileStorageServiceTest | 2 | Unit 3 |
| MenuControllerTest | 8 | Unit 3 |
| OrderServiceTest | 10 | Unit 4 |
| SseEmitterServiceTest | 2 | Unit 4 |
| OrderControllerTest | 8 | Unit 4 |
| TableOrderApplicationTests | 1 | App |

## 테스트 리포트
- HTML: `build/reports/tests/test/index.html`
- XML: `build/test-results/test/`
