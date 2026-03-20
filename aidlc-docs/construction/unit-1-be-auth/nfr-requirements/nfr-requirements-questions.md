# Unit 1-BE Auth API - NFR Requirements 질문

Auth API의 비기능 요구사항을 확정하기 위한 질문입니다.
각 질문의 [Answer]: 뒤에 선택지 알파벳을 입력해주세요.

---

## Question 1
로그인 API 응답 시간 목표는?

A) 500ms 이내 (빠른 응답)
B) 1초 이내 (일반적)
C) 2초 이내 (여유 있게)
D) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 2
JWT 토큰 갱신(refresh) 전략은?

A) Refresh Token 없음 — 만료 시 재로그인 (MVP 단순화)
B) Refresh Token 도입 — Access Token 짧게(1시간) + Refresh Token 길게(16시간)
C) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 3
비밀번호 정책 (관리자 계정)은?

A) 최소 8자, 영문+숫자 필수 (기본)
B) 최소 8자, 영문+숫자+특수문자 필수 (보안 강화)
C) 제한 없음 (MVP 단순화)
D) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 4
로그인 관련 로깅 수준은?

A) 로그인 성공/실패 모두 로깅 (감사 추적)
B) 실패만 로깅 (보안 모니터링)
C) 로깅 없음 (MVP 단순화)
D) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 5
CORS 설정 범위는? (현재 WebConfig에 설정 있음)

A) 프론트엔드 도메인만 허용 (localhost:3000, localhost:3001)
B) 모든 도메인 허용 (개발 편의)
C) Other (please describe after [Answer]: tag below)

[Answer]: A
