# Unit of Work Plan

## 개요
테이블오더 서비스를 개발 가능한 단위(Unit of Work)로 분해합니다.

---

## Questions

아래 질문에 [Answer]: 태그 뒤에 선택지 알파벳을 입력해주세요.

### Question 1
Unit 개발 순서를 어떻게 하시겠습니까?

A) Backend 먼저 → Frontend 나중에 (API 완성 후 UI 개발)
B) Unit별 Full-Stack (각 Unit마다 Backend + Frontend 함께 개발)
C) Other (please describe after [Answer]: tag below)

[Answer]: B

### Question 2
Backend를 하나의 Unit으로 할지, 도메인별로 나눌지 결정해주세요.

A) 단일 Unit - Spring Boot 앱 전체를 하나의 Unit으로 개발
B) 도메인별 분리 - 인증, 주문, 메뉴, 테이블 등 도메인별 Unit으로 분리 개발
C) Other (please describe after [Answer]: tag below)

[Answer]: B

---

## Execution Plan

### Phase 1: Unit 정의
- [x] Unit 목록 및 책임 정의
- [x] Unit별 포함 스토리 매핑
- [x] `aidlc-docs/inception/application-design/unit-of-work.md` 생성

### Phase 2: 의존성 및 순서
- [x] Unit 간 의존성 매트릭스 작성
- [x] 개발 순서 결정
- [x] `aidlc-docs/inception/application-design/unit-of-work-dependency.md` 생성

### Phase 3: Story 매핑
- [x] 모든 User Story를 Unit에 할당
- [x] 커버리지 검증
- [x] `aidlc-docs/inception/application-design/unit-of-work-story-map.md` 생성
