# Unit 1-BE Auth API - Logical Components

## 컴포넌트 구성도

```
auth/
├── domain/
│   └── Admin.java                    # 엔티티 (loginAttempts, lockedUntil 포함)
├── controller/
│   ├── AdminAuthController.java      # 관리자 로그인, 로그아웃
│   └── TableAuthController.java      # 테이블 인증, QR 인증, QR 토큰 생성
├── service/
│   └── AuthService.java              # 인증 비즈니스 로직
├── repository/
│   └── AdminRepository.java          # Admin JPA Repository
└── dto/
    ├── LoginRequest.java             # 관리자 로그인 요청
    ├── TableLoginRequest.java        # 테이블 인증 요청
    ├── QrLoginRequest.java           # QR 인증 요청
    ├── QrTokenRequest.java           # QR 토큰 생성 요청
    ├── TokenResponse.java            # Access Token 응답 (body)
    └── QrTokenResponse.java          # QR 토큰 응답

store/ (참조만 — Unit 2에서 상세 구현)
├── domain/
│   ├── Store.java                    # 최소 정의 (id, storeCode, name)
│   └── StoreTable.java              # 최소 정의 (id, storeId, tableNo, password)
└── repository/
    ├── StoreRepository.java          # findByStoreCode
    └── StoreTableRepository.java     # findByStoreIdAndTableNo

common/config/ (기존 수정)
├── JwtTokenProvider.java             # createAccessToken, createRefreshToken 분리
├── JwtAuthenticationFilter.java      # Access Token만 검증
├── SecurityConfig.java               # /api/auth/refresh permitAll 추가
└── WebConfig.java                    # CORS credentials:true 확인
```

## 변경 대상 (Unit 0 기존 코드)

| 파일 | 변경 내용 |
|------|----------|
| JwtTokenProvider.java | createAccessToken/createRefreshToken 분리, tokenType 클레임 추가 |
| JwtAuthenticationFilter.java | tokenType=ACCESS만 인증 처리 |
| SecurityConfig.java | `/api/auth/refresh`, `/api/admin/auth/logout` permitAll 추가 |
| WebConfig.java | credentials(true) 확인 |
| ErrorCode.java | 필요 시 추가 에러 코드 |

## DB 스키마 변경

```sql
ALTER TABLE admins ADD COLUMN login_attempts INT NOT NULL DEFAULT 0;
ALTER TABLE admins ADD COLUMN locked_until DATETIME NULL;
```
