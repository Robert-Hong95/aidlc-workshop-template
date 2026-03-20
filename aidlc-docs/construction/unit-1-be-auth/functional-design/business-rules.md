# Unit 1-BE Auth API - Business Rules

## BR-01: 관리자 로그인 검증
- 매장 코드 + 사용자명 + 비밀번호 3단계 검증
- 매장 코드로 먼저 매장 존재 확인 → 매장 내 username 조회 → 비밀번호 검증
- 비밀번호는 bcrypt로 해싱 저장, `PasswordEncoder.matches()`로 비교

## BR-02: 로그인 시도 횟수 제한
- 연속 5회 실패 시 15분 잠금
- 잠금 상태: `locked_until`이 현재 시각 이후이면 잠금
- 잠금 해제: `locked_until` 시각이 지나면 자동 해제
- 로그인 성공 시: `login_attempts = 0`, `locked_until = null`로 리셋
- 로그인 실패 시: `login_attempts += 1`, 5회 도달 시 `locked_until = now + 15분`

## BR-03: JWT 토큰 발급
- **관리자 JWT**: subject=adminId, claims={storeId, adminId, username, storeName, role=ADMIN}
- **테이블 JWT**: subject=tableId, claims={storeId, tableId, tableNo, storeName, role=TABLE}
- **QR JWT**: subject="qr", claims={storeCode, tableNo, type=QR}, 만료시간=10분
- 만료시간: 관리자/테이블 = 16시간 (57,600,000ms), QR = 10분 (600,000ms)

## BR-04: 테이블 인증 (태블릿)
- 매장 코드 + 테이블 번호 + 비밀번호로 인증
- 로그인 시도 횟수 제한 없음 (태블릿은 관리자가 설정하므로)
- 인증 성공 시 TABLE role JWT 발급

## BR-05: QR 코드 인증
- 관리자가 QR 토큰 생성 API 호출 → 짧은 만료시간(10분)의 JWT 생성
- QR URL 형식: `{frontend_url}/qr?token={qrJwtToken}`
- 고객이 QR 스캔 → qrToken으로 인증 API 호출 → 일반 TABLE JWT 발급
- QR JWT의 `type=QR` 클레임으로 일반 JWT와 구분
- QR JWT는 일회용이 아님 (만료시간 내 재사용 가능) — JWT 특성상 서버 저장 없음

## BR-06: 에러 응답
- 보안상 "매장 없음"과 "비밀번호 틀림"을 구분하지 않고 동일한 INVALID_CREDENTIALS 반환
  - 단, 매장 코드 자체가 틀린 경우는 STORE_NOT_FOUND 반환 (매장 코드는 공개 정보)
- 잠금 상태에서는 LOGIN_ATTEMPTS_EXCEEDED 반환 (남은 잠금 시간 포함)

## BR-07: DB 스키마 변경
- `admins` 테이블에 2개 컬럼 추가 필요:
  - `login_attempts INT NOT NULL DEFAULT 0`
  - `locked_until DATETIME NULL`
