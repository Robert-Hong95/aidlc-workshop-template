# 병렬 작업 가이드 - 테이블오더 서비스

## 개요

테이블오더 서비스를 **Backend 담당자**와 **Frontend 담당자** 2명이 병렬로 개발합니다.
각 담당자는 별도의 AI-DLC 세션에서 작업하며, 이 문서를 기준으로 작업 범위와 규칙을 따릅니다.

---

## 작업 분배

### 🔵 Backend 담당자 (Branch: `feat/backend`)

| Unit | 내용 | Stories | 상태 |
|------|------|:---:|:---:|
| Unit 1-BE | Auth API | US-A01, US-C01 | ⬜ |
| Unit 2-BE | Store/Table API | US-A05, US-A03 | ⬜ |
| Unit 3-BE | Menu API | US-A04, US-C02, US-S01 | ⬜ |
| Unit 4-BE | Order + SSE API | US-C04, US-C05, US-A02, US-S02 | ⬜ |

**작업 디렉토리**: `table-order/backend/`
**건드리지 않는 영역**: `table-order/frontend/` (Unit 0에서 생성된 초기 구조 유지)

**각 Unit 진행 순서**:
1. Functional Design (비즈니스 로직 상세 설계)
2. NFR Requirements (해당 Unit의 NFR 평가)
3. NFR Design (NFR 패턴 설계)
4. Code Generation (API 코드 생성)
5. 승인 후 다음 Unit

### 🟢 Frontend 담당자 (Branch: `feat/frontend`)

| Unit | 내용 | Stories | 상태 |
|------|------|:---:|:---:|
| Unit 5-FE | 공통 레이아웃 + Auth UI | US-A01, US-C01 | ⬜ |
| Unit 6-FE | Store/Table 관리 UI | US-A05, US-A03 | ⬜ |
| Unit 7-FE | Menu UI (관리자 + 고객) | US-A04, US-C02 | ⬜ |
| Unit 8-FE | Order + Dashboard UI | US-C03, US-C04, US-C05, US-A02 | ⬜ |

**작업 디렉토리**: `table-order/frontend/`
**건드리지 않는 영역**: `table-order/backend/` (Unit 0에서 생성된 초기 구조 유지)

**각 Unit 진행 순서**:
1. Functional Design (컴포넌트 상세 설계, API 연동 포인트)
2. Code Generation (UI 코드 생성)
3. 승인 후 다음 Unit

**참고**: Frontend는 Backend API가 완성되기 전에도 작업 가능합니다.
- `@table-order/api-client`의 타입 정의를 기반으로 개발
- API 연동은 mock 또는 Backend 완성 후 통합 테스트에서 검증

---

## Git 브랜치 전략

```
main (현재 커밋 - Unit 0 Foundation 완료)
  ├── feat/backend   ← Backend 담당자
  └── feat/frontend  ← Frontend 담당자
```

### 규칙
1. **main에서 각자 브랜치 생성** 후 작업
2. **Backend는 `table-order/backend/`만 수정**, Frontend는 `table-order/frontend/`만 수정
3. **공유 파일 충돌 방지**: `docker-compose.yml`, `.gitignore`는 수정하지 않음
4. **API 계약 변경 시**: `table-order/frontend/packages/api-client/src/types/`에 타입을 먼저 정의하고 양쪽에 공유
5. **각자 작업 완료 후 main에 머지** (Backend 먼저 권장)

### 머지 순서
```
1. feat/backend → main (Backend 완성)
2. feat/frontend → main (Frontend 완성, API 연동 확인)
3. Build and Test (통합 테스트)
```

---

## AI-DLC 세션 운영

### Backend 세션 시작 방법
1. `feat/backend` 브랜치 체크아웃
2. AI-DLC 세션 시작
3. AI에게 다음과 같이 요청:
   ```
   AI-DLC 워크플로우를 이어서 진행합니다.
   Backend 담당자로서 Unit 1-BE (Auth API)부터 시작합니다.
   aidlc-docs/aidlc-state.md와 parallel-work-guide.md를 참고해주세요.
   ```

### Frontend 세션 시작 방법
1. `feat/frontend` 브랜치 체크아웃
2. AI-DLC 세션 시작
3. AI에게 다음과 같이 요청:
   ```
   AI-DLC 워크플로우를 이어서 진행합니다.
   Frontend 담당자로서 Unit 5-FE (공통 + Auth UI)부터 시작합니다.
   aidlc-docs/aidlc-state.md와 parallel-work-guide.md를 참고해주세요.
   ```

### 세션 간 공유 사항
- **API 스펙**: Backend가 생성하는 Controller/DTO를 기준으로 Frontend가 타입 정의
- **DB 스키마**: `backend/src/main/resources/db/init.sql` (Unit 0에서 확정)
- **공유 상수**: `frontend/packages/shared/src/constants/` (Unit 0에서 생성)

---

## 참조 문서

| 문서 | 경로 | 용도 |
|------|------|------|
| 요구사항 | `aidlc-docs/inception/requirements/requirements.md` | 기능/비기능 요구사항 |
| User Stories | `aidlc-docs/inception/user-stories/stories.md` | Acceptance Criteria |
| 컴포넌트 설계 | `aidlc-docs/inception/application-design/components.md` | 전체 컴포넌트 구조 |
| 메서드 정의 | `aidlc-docs/inception/application-design/component-methods.md` | Service/Repository 시그니처 |
| 서비스 플로우 | `aidlc-docs/inception/application-design/services.md` | 오케스트레이션 패턴 |
| 의존성 | `aidlc-docs/inception/application-design/component-dependency.md` | 컴포넌트 간 관계 |
| Unit 정의 | `aidlc-docs/inception/application-design/unit-of-work.md` | 원본 Unit 분해 |
| 인프라 설계 | `aidlc-docs/construction/unit-0-foundation/infrastructure-design/infrastructure-design.md` | DB 스키마, Docker |
