# Unit of Work Dependency - 테이블오더 서비스

## 의존성 매트릭스

| Unit | 의존 대상 | 의존 유형 | 설명 |
|------|----------|----------|------|
| Unit 0 (Foundation) | 없음 | - | 최초 설정, 의존성 없음 |
| Unit 1 (Auth) | Unit 0 | 필수 | Security 설정, JWT 인프라 필요 |
| Unit 2 (Store/Table) | Unit 0, Unit 1 | 필수 | 인증 필요, DB 스키마 필요 |
| Unit 3 (Menu) | Unit 0, Unit 1, Unit 2 | 필수 | 매장 존재 필요, 인증 필요 |
| Unit 4 (Order) | Unit 0, Unit 1, Unit 2, Unit 3 | 필수 | 메뉴/테이블/매장/인증 모두 필요 |

## 의존성 다이어그램

```
Unit 0 (Foundation)
  |
  v
Unit 1 (Auth)
  |
  v
Unit 2 (Store/Table)
  |
  v
Unit 3 (Menu)
  |
  v
Unit 4 (Order)
```

## 개발 순서

| 순서 | Unit | 이유 |
|:---:|------|------|
| 1 | Unit 0 - Foundation | 모든 Unit의 기반, 프로젝트 구조/설정 |
| 2 | Unit 1 - Auth | 다른 모든 Unit이 인증에 의존 |
| 3 | Unit 2 - Store/Table | 메뉴와 주문이 매장/테이블에 의존 |
| 4 | Unit 3 - Menu | 주문이 메뉴에 의존 |
| 5 | Unit 4 - Order | 모든 도메인 완성 후 주문/SSE 구현 |

## 개발 방식
- 각 Unit은 Backend + Frontend를 함께 개발 (Full-Stack 병행)
- Unit 완료 후 다음 Unit으로 진행 (순차적)
- 각 Unit 완료 시 해당 기능의 E2E 동작 확인 가능
