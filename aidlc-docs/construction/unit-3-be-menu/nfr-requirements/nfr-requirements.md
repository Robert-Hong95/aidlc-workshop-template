# Unit 3-BE Menu API - NFR Requirements

## 공통 NFR (Unit 1-2에서 결정, 그대로 적용)
- JWT 인증, CORS, 로깅, 비밀번호 정책, API 응답 < 1초

## Unit 3-BE 특화 NFR

### NFR-MN-01: 파일 업로드
- 최대 파일 크기: 5MB (Spring Boot multipart 설정)
- 허용 Content-Type: image/jpeg, image/png
- 저장 경로: application.yml의 file.upload-dir

### NFR-MN-02: 메뉴 순서 일괄 업데이트
- 단일 트랜잭션으로 처리
- 매장 소속 검증 후 벌크 업데이트

### NFR-MN-03: 고객용 메뉴 조회 성능
- 카테고리+메뉴 한 번에 조회 (N+1 방지)
