# CONVERSATION SUMMARY

> 저장 시점: 2026-03-20T14:07:57+09:00

## OBJECTIVE
Build a table-order service (테이블오더 서비스) using the AI-DLC (AI-Driven Development Life Cycle) workflow. The project is a greenfield full-stack application with a Spring Boot backend, Turborepo monorepo frontend (Next.js 15 customer app + admin app), and MySQL database. Currently executing the **Frontend Track** in parallel with a separate Backend Track, starting from **Unit 5-FE (공통 레이아웃 + Auth UI)**.

## USER GUIDANCE
- Follow `aidlc-docs/parallel-work-guide.md` for frontend track execution
- Frontend work proceeds independently of Backend API completion — use `@table-order/api-client` type definitions, defer actual API integration
- **TDD (Test-Driven Development)** approach selected for Code Generation (user chose option A)
- Only modify `table-order/frontend/` directory; do not touch `table-order/backend/`
- Write only the ABSOLUTE MINIMAL amount of code needed to address requirements correctly

## COMPLETED

### INCEPTION PHASE — 100% Complete
- Workspace Detection, Requirements Analysis, User Stories, Workflow Planning, Application Design, Units Generation — all done

### CONSTRUCTION PHASE — Unit 0 Foundation Complete
- Project scaffolding: Spring Boot backend structure, Turborepo monorepo frontend structure, Docker Compose, MySQL init script
- `packages/` directory does NOT exist yet (only `apps/customer` and `apps/admin` with skeleton files)

### Unit 5-FE Functional Design — COMPLETE & APPROVED
Key design decisions from Q&A:
1. **Q1 - Admin Sidebar**: Mobile responsive + desktop toggle (collapsible icon ↔ full mode)
2. **Q2 - Customer BottomNav**: 3 tabs: 메뉴 | 장바구니 | 주문내역
3. **Q3 - Login attempt limit**: Apple-style progressive delay (6th fail: 1min, 7th: 5min, 8th: 15min, 9th+: 60min)
4. **Q4 - Auto-login storage**: Refresh Token approach — NO password stored in localStorage. Access Token (1h) + Refresh Token (16h). On app start, attempt token refresh; on failure, redirect to setup page
5. **Q5 - Customer theme**: AWS-style — white background + orange `#FF9900` accent + dark navy `#232F3E` text
6. **Q6 - Admin theme**: Dark sidebar (`#232F3E`) + light content (`#F2F3F3`)
7. **Q7 - Settings re-entry**: Header ⚙️ icon → password confirmation modal → then navigate to setup page

Functional Design artifacts created:
- `aidlc-docs/construction/unit-5-fe/functional-design/business-logic-model.md`
- `aidlc-docs/construction/unit-5-fe/functional-design/business-rules.md`
- `aidlc-docs/construction/unit-5-fe/functional-design/frontend-components.md`
- `aidlc-docs/construction/unit-5-fe/functional-design/domain-entities.md`

### Unit 5-FE TDD Planning — COMPLETE, AWAITING APPROVAL
- Contracts: `aidlc-docs/construction/plans/unit-5-fe-contracts.md`
- Test Plan (31 test cases): `aidlc-docs/construction/plans/unit-5-fe-test-plan.md`
- TDD Code Generation Plan (8 Steps): `aidlc-docs/construction/plans/unit-5-fe-tdd-code-generation-plan.md`

## TECHNICAL CONTEXT

### Existing Frontend File Structure
```
table-order/frontend/
├── turbo.json
├── package.json                  # pnpm@9.15.0, turbo@^2.4.0
├── tsconfig.json
├── pnpm-workspace.yaml           # packages: ["apps/*", "packages/*"]
├── apps/
│   ├── customer/                 # Port 3000 — next@^15.2, react@^19, zustand@^5, @tanstack/react-query@^5.64, tailwindcss@^4
│   └── admin/                    # Port 3001 — same deps + react-hook-form@^7.54, zod@^3.24
└── packages/                     # ⚠️ DOES NOT EXIST YET
```

### packages/ to be created
- `packages/ui/` — shared UI components (Button, Card, Input, Modal, Toast, Spinner, EmptyState, Badge, ConfirmDialog)
- `packages/api-client/` — HTTP client with auth interceptors, token refresh, concurrency queue
- `packages/shared/` — utils (format, storage, validation), constants, types

### Key Architecture Patterns
- Next.js 15 App Router with route groups: `(auth)/` and `(main)/`
- Zustand for client state (auth-store, cart-store, dashboard-store)
- TanStack Query for server state
- Tailwind CSS 4 for styling
- `data-testid` attributes required on all interactive elements

### Frontend Unit Breakdown
| Unit | Content | Stories | Status |
|------|---------|---------|--------|
| Unit 5-FE | 공통 레이아웃 + Auth UI | US-A01, US-C01 | **TDD Plan 승인 대기** |
| Unit 6-FE | Store/Table 관리 UI | US-A05, US-A03 | ⬜ |
| Unit 7-FE | Menu UI (관리자 + 고객) | US-A04, US-C02 | ⬜ |
| Unit 8-FE | Order + Dashboard UI | US-C03, US-C04, US-C05, US-A02 | ⬜ |

## NEXT STEPS
1. TDD Code Generation Plan 승인 받기
2. Part 2 실행: RED-GREEN-REFACTOR cycle로 코드 생성
3. Unit 5-FE 완료 후 Unit 6-FE 진행

## KEY REFERENCE DOCUMENTS
- Requirements: `aidlc-docs/inception/requirements/requirements.md`
- User Stories: `aidlc-docs/inception/user-stories/stories.md`
- Components: `aidlc-docs/inception/application-design/components.md`
- Component Methods: `aidlc-docs/inception/application-design/component-methods.md`
- Services: `aidlc-docs/inception/application-design/services.md`
- Parallel Work Guide: `aidlc-docs/parallel-work-guide.md`
- Infrastructure Design: `aidlc-docs/construction/unit-0-foundation/infrastructure-design/infrastructure-design.md`
