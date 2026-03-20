# Unit 2-BE Store/Table API - NFR Design Plan

## 계획

- [x] Step 1: NFR Requirements 분석
- [x] Step 2: Design Patterns 정의 (5개 패턴)
- [x] Step 3: Logical Components 정의

## 설계 패턴 요약
- DP-ST-01: Rate Limiting (Sliding Window Counter, 인메모리)
- DP-ST-02: Batch Processing (Chunk-based, 100건씩, 단일 트랜잭션)
- DP-ST-03: Optimized Query (LEFT JOIN Fetch)
- DP-ST-04: Cursor Pagination (Keyset Pagination)
- DP-ST-05: Input Validation (Bean Validation 재사용)
