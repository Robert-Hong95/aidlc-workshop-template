# Unit 2-BE Store/Table API - Tech Stack Decisions

## 기존 스택 유지 (Unit 1-BE 동일)
- Java 21, Spring Boot 3.4.3, Spring Security, jjwt 0.12.6
- MySQL 8.0, H2 (테스트)
- Gradle, JUnit 5, Mockito

## Unit 2-BE 추가 기술 결정

### Rate Limiting
- **선택**: Spring HandlerInterceptor + ConcurrentHashMap
- **이유**: 외부 라이브러리 없이 MVP 수준 구현 가능
- **대안 고려**: Bucket4j, Resilience4j → MVP에서는 과도

### JSON 직렬화 (주문 이력)
- **선택**: Jackson ObjectMapper (Spring Boot 기본 포함)
- **이유**: 추가 의존성 없음, 주문 데이터를 JSON TEXT 컬럼에 저장

### 배치 처리
- **선택**: Spring @Transactional + 수동 배치 루프
- **이유**: Spring Batch는 과도, 단순 루프로 100건씩 처리 충분

### 페이지네이션
- **선택**: Cursor 기반 (Spring Data JPA 커스텀 쿼리)
- **이유**: Offset 대비 대량 데이터에서 성능 우수
