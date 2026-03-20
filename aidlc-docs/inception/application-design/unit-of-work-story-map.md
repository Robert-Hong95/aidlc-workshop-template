# Unit of Work - Story Map

## Story → Unit 매핑

| Story | Story 제목 | Unit | Priority |
|-------|-----------|------|----------|
| US-C01 | 테이블 자동 로그인 | Unit 1 (Auth) | Must |
| US-A01 | 매장 인증 | Unit 1 (Auth) | Must |
| US-A05 | 매장 관리 | Unit 2 (Store/Table) | Must |
| US-A03 | 테이블 관리 | Unit 2 (Store/Table) | Must |
| US-A04 | 메뉴 관리 | Unit 3 (Menu) | Must |
| US-C02 | 메뉴 조회 및 탐색 | Unit 3 (Menu) | Must |
| US-S01 | 이미지 업로드 | Unit 3 (Menu) | Should |
| US-C03 | 장바구니 관리 | Unit 4 (Order) | Must |
| US-C04 | 주문 생성 | Unit 4 (Order) | Must |
| US-C05 | 주문 내역 조회 | Unit 4 (Order) | Must |
| US-A02 | 실시간 주문 모니터링 | Unit 4 (Order) | Must |
| US-S02 | SSE 실시간 통신 | Unit 4 (Order) | Must |

## Unit별 Story 수

| Unit | Story 수 | Must | Should |
|------|:--------:|:----:|:------:|
| Unit 0 (Foundation) | 0 | - | - |
| Unit 1 (Auth) | 2 | 2 | 0 |
| Unit 2 (Store/Table) | 2 | 2 | 0 |
| Unit 3 (Menu) | 3 | 2 | 1 |
| Unit 4 (Order) | 5 | 5 | 0 |
| **합계** | **12** | **11** | **1** |

## 커버리지 검증
- 전체 User Story: 12개
- Unit에 할당된 Story: 12개
- 미할당 Story: 0개
- **커버리지: 100%**
