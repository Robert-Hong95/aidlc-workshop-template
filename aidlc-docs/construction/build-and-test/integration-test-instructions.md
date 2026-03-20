# Integration Test Instructions - 테이블오더 서비스

## 사전 조건

전체 서비스가 실행 중이어야 합니다:
```bash
cd table-order/ && docker compose up --build -d
```

## 통합 테스트 시나리오

### 시나리오 1: 관리자 로그인 → 매장 확인

1. http://localhost:3001 접속
2. 매장코드: `STORE01`, 사용자명: `admin`, 비밀번호: `pass1234` 입력
3. 로그인 성공 → 대시보드 이동 확인

### 시나리오 2: 메뉴 등록

1. 관리자 로그인 후 사이드바 "메뉴" 클릭
2. "메뉴 추가" 버튼 클릭
3. 카테고리 추가 (예: "메인메뉴")
4. 메뉴 등록: 이름, 가격, 카테고리 선택 후 저장
5. 메뉴 목록에 표시 확인

### 시나리오 3: 고객 주문 플로우

1. http://localhost:3000 접속
2. 초기 설정: 매장코드 `STORE01`, 테이블번호 `1`, 비밀번호 `pass1234`
3. 메뉴 화면에서 카테고리별 메뉴 확인
4. "담기" 버튼으로 장바구니 추가
5. 장바구니 → 수량 조절 → "주문하기"
6. 주문 확인 → "주문 확정"
7. 주문 성공 모달 확인 (5초 후 메뉴로 자동 이동)

### 시나리오 4: 실시간 주문 모니터링 (SSE)

1. 관리자 대시보드 열어둔 상태에서
2. 고객앱에서 주문 생성
3. 관리자 대시보드에 신규 주문 실시간 표시 확인
4. 관리자가 주문 상태 변경 (대기중 → 준비중 → 완료)
5. 고객 주문 내역에서 상태 실시간 업데이트 확인

### 시나리오 5: 테이블 세션 관리

1. 관리자 → 테이블 관리
2. 테이블 "이용 완료" 클릭
3. 확인 팝업 → 확인
4. 고객앱에서 이전 주문 내역 사라짐 확인
5. 관리자 → 과거 내역에서 이전 주문 확인

## API 직접 테스트 (Swagger)

http://localhost:8080/swagger-ui.html 에서 모든 API를 직접 테스트할 수 있습니다.

### 인증 토큰 획득

```bash
# 관리자 로그인
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"storeCode":"STORE01","username":"admin","password":"pass1234"}'

# 테이블 로그인
curl -X POST http://localhost:8080/api/customer/auth/login \
  -H "Content-Type: application/json" \
  -d '{"storeCode":"STORE01","tableNo":1,"password":"pass1234"}'
```

응답의 `data.token` 값을 이후 요청의 `Authorization: Bearer {token}` 헤더에 사용합니다.
