# Application Design Plan

## 개요
테이블오더 서비스의 컴포넌트 구조, 서비스 레이어, 의존성 관계를 설계합니다.

---

## Design Questions

아래 질문에 [Answer]: 태그 뒤에 선택지 알파벳을 입력해주세요.

### Question 1
Backend API 아키텍처 패턴을 어떻게 구성하시겠습니까?

A) Layered Architecture - Controller → Service → Repository 3계층
B) Hexagonal Architecture - Port/Adapter 패턴으로 도메인 중심 설계
C) Other (please describe after [Answer]: tag below)

[Answer]: C로 구성하고 Layered Architecture 기반 + DDD 개발 방식 적용해줘

### Question 2
Frontend(Next.js) 프로젝트 구성을 어떻게 하시겠습니까?

A) 단일 Next.js 프로젝트 - 고객용/관리자용을 라우팅으로 분리 (예: /customer/*, /admin/*)
B) 별도 Next.js 프로젝트 2개 - 고객용, 관리자용 각각 독립 프로젝트
C) Other (please describe after [Answer]: tag below)

[Answer]: C, 모노레포로 구성해서, 최대한 모노레포에 이점을 살려서 유틸이나 설정들을 공유하고 B로 만들어줘. 최신 개발 트렌드도 적용해줘

### Question 3
Backend API URL 구조를 어떻게 설계하시겠습니까?

A) 역할 기반 분리 - /api/customer/*, /api/admin/* 으로 구분
B) 리소스 기반 통합 - /api/orders, /api/menus 등 리소스 중심 (인증으로 권한 구분)
C) Other (please describe after [Answer]: tag below)

[Answer]: A

---

## Execution Plan

### Phase 1: 컴포넌트 식별
- [x] Backend 컴포넌트 식별 (Controller, Service, Repository, Domain)
- [x] Frontend 컴포넌트 식별 (Pages, Components, Hooks, API Client)
- [x] `aidlc-docs/inception/application-design/components.md` 생성

### Phase 2: 컴포넌트 메서드 정의
- [x] 각 Service 컴포넌트의 메서드 시그니처 정의
- [x] 각 Repository 컴포넌트의 메서드 시그니처 정의
- [x] `aidlc-docs/inception/application-design/component-methods.md` 생성

### Phase 3: 서비스 레이어 설계
- [x] 서비스 간 오케스트레이션 패턴 정의
- [x] SSE 이벤트 흐름 설계
- [x] `aidlc-docs/inception/application-design/services.md` 생성

### Phase 4: 의존성 관계 정의
- [x] 컴포넌트 간 의존성 매트릭스 작성
- [x] 데이터 흐름 다이어그램 작성
- [x] `aidlc-docs/inception/application-design/component-dependency.md` 생성
