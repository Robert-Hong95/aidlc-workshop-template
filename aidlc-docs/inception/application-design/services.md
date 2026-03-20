# Services - 테이블오더 서비스

## 서비스 오케스트레이션 패턴

### 1. 주문 생성 플로우
```
CustomerOrderController.createOrder()
  → OrderService.createOrder()
    → TableService.getActiveSession()     // 세션 확인 (없으면 자동 시작)
    → OrderRepository.save()              // 주문 저장
    → SseEmitterService.publishToStore()  // 관리자에게 신규 주문 알림
    → return Order
```

### 2. 주문 상태 변경 플로우
```
AdminOrderController.updateStatus()
  → OrderService.updateOrderStatus()
    → OrderRepository.findById()                // 주문 조회
    → Order.changeStatus()                      // 상태 전이 검증
    → OrderRepository.save()                    // 저장
    → SseEmitterService.publishToStore()        // 관리자에게 상태 변경 알림
    → SseEmitterService.publishToTable()        // 고객에게 상태 변경 알림
    → return Order
```

### 3. 테이블 이용 완료 플로우
```
AdminTableController.endSession()
  → TableService.endSession()
    → TableSessionRepository.findActiveByTableId()  // 활성 세션 조회
    → OrderService.getOrdersBySession()              // 세션 주문 조회
    → OrderHistoryRepository.saveAll()               // 주문 이력으로 이동
    → OrderRepository.deleteAll()                    // 현재 주문 삭제
    → TableSession.end()                             // 세션 종료
    → SseEmitterService.publishToStore()             // 관리자에게 테이블 리셋 알림
```

### 4. 관리자 인증 플로우
```
AdminAuthController.login()
  → AuthService.loginAdmin()
    → StoreRepository.findByStoreCode()    // 매장 확인
    → AdminRepository.findByStoreIdAndUsername()  // 관리자 조회
    → PasswordEncoder.matches()            // 비밀번호 검증
    → JwtTokenProvider.createToken()       // JWT 생성 (16시간)
    → return TokenResponse
```

### 5. 테이블 인증 플로우
```
TableAuthController.login()
  → AuthService.loginTable()
    → StoreRepository.findByStoreCode()                    // 매장 확인
    → TableRepository.findByStoreIdAndTableNo()            // 테이블 확인
    → PasswordEncoder.matches()                            // 비밀번호 검증
    → JwtTokenProvider.createToken()                       // JWT 생성
    → return TokenResponse
```

---

## SSE 이벤트 설계

### 이벤트 타입
| Event Type | 대상 | Payload | 트리거 |
|------------|------|---------|--------|
| NEW_ORDER | 매장 관리자 | Order 정보 | 고객 주문 생성 |
| ORDER_STATUS_CHANGED | 매장 관리자 + 해당 테이블 고객 | orderId, newStatus | 관리자 상태 변경 |
| ORDER_DELETED | 매장 관리자 + 해당 테이블 고객 | orderId | 관리자 주문 삭제 |
| TABLE_RESET | 매장 관리자 | tableId | 테이블 이용 완료 |

### SSE 연결 관리
- 매장별 관리자 연결 풀: Map<storeId, List\<SseEmitter\>>
- 매장+테이블별 고객 연결 풀: Map<storeId:tableId, List\<SseEmitter\>>
- Heartbeat: 30초 간격
- 연결 타임아웃: 30분 (자동 재연결)
