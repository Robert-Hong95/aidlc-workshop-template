# User Stories - 테이블오더 서비스

**분류 방식**: User Journey-Based
**Acceptance Criteria 수준**: 상세 (Given/When/Then 5개 이상)
**Story 크기**: Feature 수준
**우선순위**: MoSCoW

---

## Journey 1: 고객 주문 플로우

### US-C01: 테이블 자동 로그인
**As a** 고객,
**I want** 태블릿에서 별도 로그인 없이 자동으로 인증되기를,
**So that** 즉시 메뉴를 보고 주문할 수 있다.

**Priority**: Must
**Persona**: 고객 (김민수)
**Requirement**: FR-C01

**Acceptance Criteria**:
```gherkin
Given 관리자가 태블릿에 매장ID, 테이블번호, 비밀번호를 최초 설정했을 때
When 태블릿에서 앱을 열면
Then 저장된 정보로 자동 로그인되어 메뉴 화면이 표시된다

Given 자동 로그인 정보가 로컬에 저장되어 있을 때
When 브라우저를 새로고침하면
Then 재로그인 없이 메뉴 화면이 유지된다

Given 저장된 로그인 정보가 없을 때
When 태블릿에서 앱을 열면
Then 초기 설정 화면(매장ID, 테이블번호, 비밀번호 입력)이 표시된다

Given 잘못된 인증 정보가 저장되어 있을 때
When 자동 로그인을 시도하면
Then 에러 메시지와 함께 초기 설정 화면으로 이동한다

Given QR 코드로 모바일 웹에 접속할 때
When QR 코드를 스캔하면
Then 해당 매장/테이블 정보가 포함된 주문 페이지가 표시된다

Given 자동 로그인이 성공했을 때
When 세션이 유효한 동안
Then 매장ID와 테이블번호가 화면에 표시된다
```

---

### US-C02: 메뉴 조회 및 탐색
**As a** 고객,
**I want** 카테고리별로 메뉴를 탐색하고 상세 정보를 확인하기를,
**So that** 원하는 메뉴를 쉽게 찾아 선택할 수 있다.

**Priority**: Must
**Persona**: 고객 (김민수)
**Requirement**: FR-C02

**Acceptance Criteria**:
```gherkin
Given 로그인된 상태에서
When 앱을 열면
Then 메뉴 화면이 기본 화면으로 표시된다

Given 메뉴 화면에서
When 카테고리 목록을 보면
Then 모든 카테고리가 표시되고 각 카테고리를 탭하면 해당 메뉴로 빠르게 이동한다

Given 메뉴 목록에서
When 각 메뉴 카드를 보면
Then 메뉴명, 가격, 설명, 이미지가 카드 형태로 표시된다

Given 메뉴 화면에서
When 터치로 조작할 때
Then 모든 버튼이 최소 44x44px 이상이며 터치 친화적이다

Given 메뉴가 여러 카테고리에 걸쳐 있을 때
When 카테고리 간 이동할 때
Then 스크롤 또는 탭으로 빠르게 전환된다

Given 관리자가 아직 메뉴를 등록하지 않은 매장에서
When 고객이 태블릿으로 자동 로그인되어 메뉴 화면이 표시되면
Then "등록된 메뉴가 없습니다" 안내 메시지가 표시된다
```

---

### US-C03: 장바구니 관리
**As a** 고객,
**I want** 주문 전에 선택한 메뉴를 장바구니에서 확인하고 수정하기를,
**So that** 최종 주문 전에 내용을 검토하고 조정할 수 있다.

**Priority**: Must
**Persona**: 고객 (김민수)
**Requirement**: FR-C03

**Acceptance Criteria**:
```gherkin
Given 메뉴 화면에서
When 메뉴 항목의 추가 버튼을 탭하면
Then 해당 메뉴가 장바구니에 추가되고 수량이 1로 설정된다

Given 장바구니에 메뉴가 있을 때
When 수량 증가/감소 버튼을 탭하면
Then 수량이 변경되고 총 금액이 실시간으로 재계산된다

Given 장바구니에 메뉴가 있을 때
When 삭제 버튼을 탭하면
Then 해당 메뉴가 장바구니에서 제거된다

Given 장바구니에 메뉴가 있을 때
When 장바구니 비우기를 탭하면
Then 모든 메뉴가 제거되고 총 금액이 0원이 된다

Given 장바구니에 메뉴를 담은 상태에서
When 페이지를 새로고침하면
Then 장바구니 내용이 로컬 저장소에서 복원된다

Given 장바구니가 비어있을 때
When 장바구니를 열면
Then "장바구니가 비어있습니다" 메시지와 메뉴로 이동 버튼이 표시된다

Given 장바구니에 여러 메뉴가 있을 때
When 총 금액을 확인하면
Then 각 메뉴의 (단가 × 수량) 합계가 정확히 표시된다
```

---

### US-C04: 주문 생성
**As a** 고객,
**I want** 장바구니의 메뉴를 주문으로 확정하기를,
**So that** 매장에서 음식을 준비받을 수 있다.

**Priority**: Must
**Persona**: 고객 (김민수)
**Requirement**: FR-C04

**Acceptance Criteria**:
```gherkin
Given 장바구니에 메뉴가 있을 때
When 주문 확정 버튼을 탭하면
Then 주문 내역 최종 확인 화면이 표시된다

Given 주문 확인 화면에서
When 최종 주문 버튼을 탭하면
Then 주문이 서버로 전송된다

Given 주문이 성공했을 때
When 서버 응답을 받으면
Then 주문 번호가 표시되고 장바구니가 비워지고 5초 후 메뉴 화면으로 자동 리다이렉트된다

Given 주문이 실패했을 때
When 서버 에러 응답을 받으면
Then 에러 메시지가 표시되고 장바구니 내용은 유지된다

Given 주문 전송 중일 때
When 네트워크 요청이 진행 중이면
Then 로딩 인디케이터가 표시되고 중복 주문 방지를 위해 버튼이 비활성화된다

Given 주문이 생성될 때
When 주문 데이터가 서버로 전송되면
Then 매장ID, 테이블ID, 메뉴목록(메뉴명,수량,단가), 총금액, 세션ID가 포함된다
```

---

### US-C05: 주문 내역 조회
**As a** 고객,
**I want** 현재 테이블의 주문 이력과 상태를 확인하기를,
**So that** 주문이 정상 처리되고 있는지 알 수 있다.

**Priority**: Must
**Persona**: 고객 (김민수)
**Requirement**: FR-C05

**Acceptance Criteria**:
```gherkin
Given 주문 내역 화면에서
When 주문 목록을 보면
Then 현재 테이블 세션의 주문만 시간 순으로 표시된다

Given 각 주문 항목에서
When 상세 정보를 보면
Then 주문번호, 주문시각, 메뉴/수량, 금액, 상태(대기중/준비중/완료)가 표시된다

Given 관리자가 주문 상태를 변경했을 때
When SSE 이벤트를 수신하면
Then 해당 주문의 상태가 실시간으로 업데이트된다

Given 테이블 세션이 종료(이용 완료)된 후
When 새 세션에서 주문 내역을 조회하면
Then 이전 세션의 주문은 표시되지 않는다

Given 주문 내역이 많을 때
When 목록을 스크롤하면
Then 페이지네이션 또는 무한 스크롤로 추가 주문이 로드된다

Given 현재 세션에 주문이 없을 때
When 주문 내역 화면을 열면
Then "주문 내역이 없습니다" 메시지가 표시된다
```


---

## Journey 2: 관리자 매장 운영 플로우

### US-A01: 매장 인증
**As a** 매장 관리자,
**I want** 매장 식별자와 계정으로 로그인하기를,
**So that** 내 매장의 관리 시스템에 접근할 수 있다.

**Priority**: Must
**Persona**: 관리자 (박서연)
**Requirement**: FR-A01

**Acceptance Criteria**:
```gherkin
Given 관리자 로그인 화면에서
When 매장ID, 사용자명, 비밀번호를 입력하고 로그인 버튼을 클릭하면
Then 인증 성공 시 관리자 대시보드로 이동한다

Given 로그인 성공 후
When 세션이 유지되는 동안
Then 16시간 동안 JWT 토큰 기반으로 인증이 유지된다

Given 로그인된 상태에서
When 브라우저를 새로고침하면
Then 세션이 유지되어 재로그인 없이 대시보드가 표시된다

Given 16시간이 경과했을 때
When 다음 요청을 보내면
Then 자동 로그아웃되어 로그인 화면으로 이동한다

Given 잘못된 인증 정보를 입력했을 때
When 로그인을 시도하면
Then 에러 메시지가 표시되고 로그인 시도 횟수가 제한된다

Given 비밀번호가 저장될 때
When 서버에서 처리하면
Then bcrypt로 해싱되어 안전하게 저장된다
```

---

### US-A02: 실시간 주문 모니터링
**As a** 매장 관리자,
**I want** 들어오는 주문을 실시간으로 확인하고 상태를 관리하기를,
**So that** 주문을 놓치지 않고 효율적으로 처리할 수 있다.

**Priority**: Must
**Persona**: 관리자 (박서연)
**Requirement**: FR-A02

**Acceptance Criteria**:
```gherkin
Given 관리자 대시보드에서
When 주문 모니터링 화면을 보면
Then 테이블별 카드가 그리드 레이아웃으로 표시된다

Given 각 테이블 카드에서
When 카드 내용을 보면
Then 테이블번호, 총 주문액, 최신 주문 미리보기가 표시된다

Given 고객이 새 주문을 생성했을 때
When SSE 이벤트를 수신하면
Then 2초 이내에 해당 테이블 카드에 주문이 표시되고 시각적으로 강조된다

Given 테이블 카드에서
When 주문 카드를 클릭하면
Then 전체 메뉴 목록 상세 보기가 표시된다

Given 주문 상세에서
When 상태 변경 버튼을 클릭하면
Then 주문 상태가 대기중→준비중→완료로 변경된다

Given 대시보드에서
When 테이블별 필터링을 적용하면
Then 선택한 테이블의 주문만 표시된다

Given 신규 주문이 들어왔을 때
When 대시보드에 표시되면
Then 색상 변경 또는 애니메이션으로 시각적 강조가 적용된다
```

---

### US-A03: 테이블 관리
**As a** 매장 관리자,
**I want** 테이블별 주문 상태와 세션을 관리하기를,
**So that** 테이블 회전율을 높이고 정확한 주문 관리를 할 수 있다.

**Priority**: Must
**Persona**: 관리자 (박서연)
**Requirement**: FR-A03

**Acceptance Criteria**:
```gherkin
Given 테이블 관리 화면에서
When 테이블 초기 설정을 수행하면
Then 테이블번호, 비밀번호가 설정되고 16시간 세션이 생성된다

Given 특정 주문을 삭제해야 할 때
When 주문 삭제 버튼을 클릭하면
Then 확인 팝업이 표시되고 확인 시 주문이 즉시 삭제된다

Given 주문이 삭제된 후
When 테이블 카드를 확인하면
Then 총 주문액이 재계산되어 표시된다

Given 고객이 식사를 마쳤을 때
When 테이블 이용 완료 버튼을 클릭하면
Then 확인 팝업 후 해당 세션의 주문이 과거 이력으로 이동하고 테이블이 리셋된다

Given 테이블 세션이 종료된 후
When 새 고객이 주문을 시작하면
Then 이전 주문 내역 없이 새 세션으로 시작된다

Given 과거 내역 버튼을 클릭했을 때
When 과거 주문 목록이 표시되면
Then 시간 역순으로 주문번호, 시각, 메뉴목록, 총금액, 이용완료 시각이 표시된다

Given 과거 내역 화면에서
When 날짜 필터링을 적용하면
Then 선택한 날짜 범위의 주문만 표시된다

Given 과거 내역 화면에서
When 닫기 버튼을 클릭하면
Then 대시보드로 복귀한다
```

---

### US-A04: 메뉴 관리
**As a** 매장 관리자,
**I want** 메뉴를 등록, 수정, 삭제하기를,
**So that** 매장 메뉴를 실시간으로 관리할 수 있다.

**Priority**: Must
**Persona**: 관리자 (박서연)
**Requirement**: FR-A04

**Acceptance Criteria**:
```gherkin
Given 메뉴 관리 화면에서
When 카테고리를 선택하면
Then 해당 카테고리의 메뉴 목록이 표시된다

Given 메뉴 등록 화면에서
When 메뉴명, 가격, 설명, 카테고리, 이미지를 입력하고 저장하면
Then 새 메뉴가 등록되고 목록에 표시된다

Given 메뉴 등록 시
When 필수 필드(메뉴명, 가격, 카테고리)가 비어있으면
Then 검증 에러 메시지가 표시되고 저장이 차단된다

Given 메뉴 등록 시
When 가격이 유효 범위를 벗어나면
Then 가격 범위 검증 에러가 표시된다

Given 기존 메뉴를 수정할 때
When 정보를 변경하고 저장하면
Then 메뉴 정보가 업데이트되고 고객 화면에 즉시 반영된다

Given 메뉴를 삭제할 때
When 삭제 버튼을 클릭하면
Then 확인 후 메뉴가 삭제되고 목록에서 제거된다

Given 메뉴 목록에서
When 노출 순서를 조정하면
Then 변경된 순서가 저장되고 고객 화면에 반영된다
```

---

### US-A05: 매장 관리
**As a** 매장 관리자,
**I want** 다중 매장을 등록하고 관리하기를,
**So that** 여러 매장을 하나의 시스템에서 운영할 수 있다.

**Priority**: Must
**Persona**: 관리자 (박서연)
**Requirement**: FR-A05

**Acceptance Criteria**:
```gherkin
Given 매장 관리 화면에서
When 새 매장 등록을 요청하면
Then 매장 정보 입력 폼이 표시된다

Given 매장 정보를 입력하고 저장할 때
When 필수 정보가 모두 입력되면
Then 새 매장이 등록되고 매장 목록에 표시된다

Given 다중 매장이 등록되어 있을 때
When 매장을 선택하면
Then 해당 매장의 대시보드로 전환된다

Given 매장 등록 API를 호출할 때
When 유효한 매장 정보를 전송하면
Then 매장이 생성되고 매장ID가 반환된다

Given 매장 등록 시
When 필수 정보가 누락되면
Then 검증 에러가 반환된다
```

---

## Journey 3: 시스템 지원 플로우

### US-S01: 이미지 업로드
**As a** 매장 관리자,
**I want** 메뉴 이미지를 업로드하기를,
**So that** 고객이 메뉴 사진을 보고 선택할 수 있다.

**Priority**: Should
**Persona**: 관리자 (박서연)
**Requirement**: FR-S01

**Acceptance Criteria**:
```gherkin
Given 메뉴 등록/수정 화면에서
When 이미지 파일을 선택하여 업로드하면
Then 서버 로컬 파일 시스템에 저장되고 이미지 URL이 반환된다

Given 이미지가 업로드된 후
When 메뉴 화면에서 해당 메뉴를 조회하면
Then 업로드된 이미지가 표시된다

Given 지원하지 않는 파일 형식을 업로드할 때
When 서버에서 검증하면
Then 에러 메시지가 반환된다

Given 파일 크기가 제한을 초과할 때
When 업로드를 시도하면
Then 파일 크기 초과 에러가 반환된다

Given 이미지 없이 메뉴를 등록할 때
When 메뉴를 저장하면
Then 기본 플레이스홀더 이미지가 표시된다
```

---

### US-S02: SSE 실시간 통신
**As a** 시스템,
**I want** SSE를 통해 실시간 이벤트를 전달하기를,
**So that** 관리자와 고객이 주문 상태를 즉시 확인할 수 있다.

**Priority**: Must
**Persona**: 관리자 (박서연), 고객 (김민수)
**Requirement**: FR-S02

**Acceptance Criteria**:
```gherkin
Given 관리자가 대시보드에 접속해 있을 때
When 고객이 새 주문을 생성하면
Then 2초 이내에 SSE로 신규 주문 이벤트가 전달된다

Given 관리자가 주문 상태를 변경했을 때
When 상태가 업데이트되면
Then 해당 테이블의 고객에게 SSE로 상태 변경 이벤트가 전달된다

Given SSE 연결이 끊어졌을 때
When 클라이언트가 감지하면
Then 자동으로 재연결을 시도한다

Given 여러 관리자가 동시에 접속해 있을 때
When 주문 이벤트가 발생하면
Then 같은 매장의 모든 관리자에게 이벤트가 전달된다

Given SSE 연결이 장시간 유지될 때
When 서버에서 heartbeat를 전송하면
Then 연결이 유지되고 타임아웃이 방지된다
```

---

## Persona-Story 매핑

| Story | 고객 (김민수) | 관리자 (박서연) |
|-------|:---:|:---:|
| US-C01 테이블 자동 로그인 | ✅ | |
| US-C02 메뉴 조회 | ✅ | |
| US-C03 장바구니 관리 | ✅ | |
| US-C04 주문 생성 | ✅ | |
| US-C05 주문 내역 조회 | ✅ | |
| US-A01 매장 인증 | | ✅ |
| US-A02 실시간 주문 모니터링 | | ✅ |
| US-A03 테이블 관리 | | ✅ |
| US-A04 메뉴 관리 | | ✅ |
| US-A05 매장 관리 | | ✅ |
| US-S01 이미지 업로드 | | ✅ |
| US-S02 SSE 실시간 통신 | ✅ | ✅ |

## 요구사항 커버리지

| Requirement | Story | 커버 여부 |
|-------------|-------|:---:|
| FR-C01 | US-C01 | ✅ |
| FR-C02 | US-C02 | ✅ |
| FR-C03 | US-C03 | ✅ |
| FR-C04 | US-C04 | ✅ |
| FR-C05 | US-C05 | ✅ |
| FR-A01 | US-A01 | ✅ |
| FR-A02 | US-A02 | ✅ |
| FR-A03 | US-A03 | ✅ |
| FR-A04 | US-A04 | ✅ |
| FR-A05 | US-A05 | ✅ |
| FR-S01 | US-S01 | ✅ |
| FR-S02 | US-S02 | ✅ |
