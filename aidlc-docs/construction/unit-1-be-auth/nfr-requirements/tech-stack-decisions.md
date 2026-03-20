# Unit 1-BE Auth API - Tech Stack Decisions

## 확정 기술 스택 (Unit 0에서 결정)
| 영역 | 기술 | 버전 |
|------|------|------|
| Runtime | Java | 21 |
| Framework | Spring Boot | 3.4.3 |
| Security | Spring Security | (Boot 관리) |
| JWT | jjwt (io.jsonwebtoken) | 0.12.6 |
| DB | MySQL | 8.0 |
| ORM | Spring Data JPA / Hibernate | (Boot 관리) |
| Password | BCryptPasswordEncoder | (Spring Security 내장) |
| Build | Gradle | (wrapper) |

## Unit 1-BE 추가 결정사항

### JWT 토큰 구조
- Access Token: 1시간 만료, claims에 `tokenType=ACCESS`
- Refresh Token: 16시간 만료, claims에 `tokenType=REFRESH`
- Refresh Token 저장: 서버 저장 없음 (JWT 자체 검증)

### 비밀번호 검증
- 정규식: `^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,}$`
- 검증 위치: DTO validation annotation (Unit 2에서 사용)

### 로깅
- SLF4J + Logback (Spring Boot 기본)
- 로그인 실패: WARN 레벨

### 테스트
- H2 인메모리 DB (test profile)
- Spring Security Test
