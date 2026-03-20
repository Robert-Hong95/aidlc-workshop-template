# Unit 1-BE Auth API - NFR Requirements

## NFR-AUTH-01: 성능
- 로그인 API 응답 시간: 1초 이내
- bcrypt 해싱 라운드: 10 (기본값, ~100ms)
- JWT 생성/검증: 10ms 이내

## NFR-AUTH-02: 보안 - 토큰 전략
- **Access Token**: 만료 1시간 (3,600,000ms), JWT
- **Refresh Token**: 만료 16시간 (57,600,000ms), JWT
- Access Token 만료 시 Refresh Token으로 갱신
- Refresh Token 만료 시 재로그인 필요
- Refresh Token은 DB에 저장하지 않음 (JWT 자체 검증) — MVP 단순화
- Access/Refresh 구분: JWT 클레임에 `tokenType=ACCESS|REFRESH` 포함

## NFR-AUTH-03: 보안 - 비밀번호 정책
- 최소 8자
- 영문(대소문자 무관) 1자 이상 필수
- 숫자 1자 이상 필수
- 특수문자 1자 이상 필수
- 검증 시점: 매장+관리자 등록 API (Unit 2-BE)
- 저장: bcrypt 해싱

## NFR-AUTH-04: 보안 - 로그인 시도 제한
- 연속 5회 실패 시 15분 잠금 (Functional Design BR-02)
- DB 기반 추적 (login_attempts, locked_until)

## NFR-AUTH-05: 보안 - 로깅
- 로그인 실패 시 로깅 (username, IP, 시각, 실패 사유)
- SLF4J WARN 레벨
- 성공은 로깅하지 않음

## NFR-AUTH-06: 보안 - CORS
- 허용 도메인: `http://localhost:3000` (customer), `http://localhost:3001` (admin)
- 허용 메서드: GET, POST, PUT, DELETE, OPTIONS
- 허용 헤더: Authorization, Content-Type
- Credentials: true

## NFR-AUTH-07: 보안 - QR 토큰
- QR JWT 만료: 10분 (600,000ms)
- QR JWT → 일반 Access + Refresh Token 발급

## Functional Design 업데이트 사항
Refresh Token 도입으로 인한 변경:
- `TokenResponse`에 `refreshToken` 필드 추가
- Refresh API 엔드포인트 추가: `POST /api/auth/refresh`
- JwtTokenProvider에 Access/Refresh 토큰 생성 분리
