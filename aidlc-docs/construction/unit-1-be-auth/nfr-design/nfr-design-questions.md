# Unit 1-BE Auth API - NFR Design 질문

NFR 패턴 설계를 위한 질문입니다.
각 질문의 [Answer]: 뒤에 선택지 알파벳을 입력해주세요.

---

## Question 1
Refresh Token 갱신 시 Refresh Token 자체도 새로 발급할까요? (Token Rotation)

A) Rotation 적용 — Refresh 사용 시 새 Access + 새 Refresh 발급 (보안 강화, Refresh Token 탈취 방지)
B) Rotation 미적용 — Refresh 사용 시 새 Access만 발급, Refresh는 유지 (단순)
C) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 2
Refresh Token 전달 방식은?

A) Response Body (JSON) — 프론트엔드에서 localStorage/메모리에 저장
B) HttpOnly Cookie — 브라우저가 자동 전송, XSS 방어 강화
C) Other (please describe after [Answer]: tag below)

[Answer]: B
