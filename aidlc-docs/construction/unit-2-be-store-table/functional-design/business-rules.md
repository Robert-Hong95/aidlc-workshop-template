# Unit 2-BE Store/Table API - Business Rules

## BR-ST-01: 매장 등록
- store_code는 시스템 전체에서 유일 (UNIQUE 제약)
- 매장+관리자 동시 생성 (단일 트랜잭션)
- 관리자 비밀번호 정책: 최소 8자, 영문+숫자+특수문자 필수
- 비밀번호 bcrypt 해싱 저장
- 매장 등록 API는 인증 불필요 (회원가입 성격)

## BR-ST-02: 매장 조회
- 인증된 관리자만 조회 가능
- 현재는 모든 매장 목록 반환 (향후 관리자별 필터링 가능)

## BR-ST-03: 테이블 초기 설정
- 매장 내 테이블 번호 유일 (UNIQUE(store_id, table_no))
- 테이블 비밀번호 bcrypt 해싱 저장
- 설정 시 세션 시작하지 않음 (고객 첫 주문 시 자동 시작)

## BR-ST-04: 테이블 세션 관리
- 테이블당 활성 세션 최대 1개 (ended_at IS NULL)
- 세션 시작: startSession() — 이미 활성 세션 있으면 그대로 반환
- 세션 종료: endSession() — 주문 이력 이동 후 세션 종료

## BR-ST-05: 테이블 이용 완료
- 세션의 모든 주문을 JSON 직렬화 → order_history 저장
- orders, order_items에서 해당 세션 주문 삭제
- session.ended_at = now 설정
- 활성 세션 없으면 SESSION_NOT_FOUND 예외

## BR-ST-06: 과거 주문 내역 조회
- Cursor 기반 페이지네이션 (lastId + size)
- 날짜 범위 필터링 (dateFrom, dateTo)
- 시간 역순 정렬 (최신 먼저)
- 기본 size: 20

## BR-ST-07: 관리자-매장 관계
- N:1 — 매장 1개에 관리자 여러 명 가능
- 현재 DB 구조(admins.store_id FK)로 지원
