# Unit 2-BE Store/Table API - NFR Requirements Plan

## Unit 정보
- **Unit**: Unit 2-BE (Store/Table API)

## 계획

- [x] Step 1: NFR 질문 수집 및 분석
- [x] Step 2: NFR Requirements 문서 생성
- [x] Step 3: Tech Stack Decisions 문서 생성

## 확정된 NFR 결정
- Q1=B: IP 기반 Rate Limiting (분당 10회)
- Q2=B: 배치 처리 (100건씩)
- Q3=C: 1년 보관 후 자동 삭제
- Q4=B: store_code 형식 제한 없음
- Q5=A: JOIN 쿼리로 한 번에 조회
