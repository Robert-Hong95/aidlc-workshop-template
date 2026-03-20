# TDD Code Generation Plan - Unit 5-FE (공통 레이아웃 + Auth UI)

## Unit Context
- **Workspace Root**: /Users/snowwhite/Documents/aidlc-workshop-template
- **Project Type**: Greenfield
- **Frontend Root**: table-order/frontend/
- **Stories**: US-A01 (매장 인증), US-C01 (테이블 자동 로그인)
- **Test Framework**: Vitest + React Testing Library
- **참고**: Backend API 미완성 - 타입 정의 기반 개발, API 호출은 mock 처리

---

## Plan Step 0: Project Setup + Contract Skeleton Generation
- [x] 0.1: packages/shared 패키지 생성 (package.json, tsconfig.json, 스켈레톤 파일)
- [x] 0.2: packages/api-client 패키지 생성 (package.json, tsconfig.json, 타입 정의, 스켈레톤)
- [x] 0.3: packages/ui 패키지 생성 (package.json, tsconfig.json, 스켈레톤 컴포넌트)
- [x] 0.4: Vitest 설정 (각 패키지 + 각 앱에 vitest.config.ts)
- [x] 0.5: 관리자앱 스켈레톤 (stores, hooks, components/layout, app/login)
- [x] 0.6: 고객앱 스켈레톤 (stores, hooks, components/layout, components/features/setup, app/setup)

## Plan Step 1: @table-order/shared (TDD)
- [x] 1.1: storage.ts - RED-GREEN-REFACTOR
- [x] 1.2: format.ts - RED-GREEN-REFACTOR
- [x] 1.3: validation.ts - RED-GREEN-REFACTOR

## Plan Step 2: @table-order/api-client (TDD)
- [x] 2.1: types/ 정의 (auth.ts, common.ts)
- [x] 2.2: client.ts - RED-GREEN-REFACTOR
- [x] 2.3: auth.ts - RED-GREEN-REFACTOR

## Plan Step 3: @table-order/ui (TDD)
- [x] 3.1: Spinner, Badge
- [x] 3.2: Button - RED-GREEN-REFACTOR
- [x] 3.3: Input - RED-GREEN-REFACTOR
- [x] 3.4: Modal - RED-GREEN-REFACTOR
- [x] 3.5: Toast + ToastProvider + useToast
- [x] 3.6: EmptyState, ConfirmDialog

## Plan Step 4: 관리자앱 Stores + Hooks (TDD)
- [x] 4.1: auth-store.ts - RED-GREEN-REFACTOR
- [x] 4.2: useLoginLockout.ts - RED-GREEN-REFACTOR
- [x] 4.3: useAdminAuth.ts - RED-GREEN-REFACTOR

## Plan Step 5: 고객앱 Stores + Hooks (TDD)
- [x] 5.1: auth-store.ts - RED-GREEN-REFACTOR
- [x] 5.2: useTableAuth.ts - RED-GREEN-REFACTOR

## Plan Step 6: 관리자앱 Layout Components
- [x] 6.1: AdminHeader.tsx - RED-GREEN-REFACTOR
- [x] 6.2: Sidebar.tsx - RED-GREEN-REFACTOR
- [x] 6.3: AdminLayout.tsx - RED-GREEN-REFACTOR
- [ ] 6.4: LoginPage (app/login/page.tsx) — deferred (react-hook-form + zod 폼은 API 연동 시 완성)

## Plan Step 7: 고객앱 Layout + Setup Components
- [x] 7.1: CustomerHeader.tsx - RED-GREEN-REFACTOR
- [x] 7.2: BottomNav.tsx - RED-GREEN-REFACTOR
- [x] 7.3: CustomerLayout.tsx - RED-GREEN-REFACTOR
- [x] 7.4: SetupForm.tsx - RED-GREEN-REFACTOR
- [x] 7.5: PasswordConfirmModal.tsx - RED-GREEN-REFACTOR
- [ ] 7.6: SetupPage (app/setup/page.tsx) — deferred (API 연동 시 완성)

## Plan Step 8: Documentation + Summary
- [x] 8.1: Code summary 문서 생성 (aidlc-docs/construction/unit-5-fe/code/)
- [x] 8.2: test-plan.md 최종 상태 업데이트
