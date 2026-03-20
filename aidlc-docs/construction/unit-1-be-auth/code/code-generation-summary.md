# Unit 1-BE Auth API - Code Generation Summary

## 생성/수정 파일 목록

### 신규 생성 (14 files)
| 파일 | 설명 |
|------|------|
| `auth/domain/Admin.java` | Admin 엔티티 (isLocked, incrementLoginAttempts, resetLoginAttempts) |
| `auth/repository/AdminRepository.java` | Admin JPA Repository |
| `auth/service/AuthService.java` | 인증 비즈니스 로직 (5개 메서드) |
| `auth/controller/AdminAuthController.java` | 관리자 로그인/로그아웃 |
| `auth/controller/TableAuthController.java` | 테이블 인증, QR 인증 |
| `auth/controller/AuthCommonController.java` | 토큰 갱신 (Refresh) |
| `auth/controller/QrTokenController.java` | QR 토큰 생성 |
| `auth/dto/LoginRequest.java` | 관리자 로그인 요청 DTO |
| `auth/dto/TableLoginRequest.java` | 테이블 인증 요청 DTO |
| `auth/dto/QrLoginRequest.java` | QR 인증 요청 DTO |
| `auth/dto/QrTokenRequest.java` | QR 토큰 생성 요청 DTO |
| `auth/dto/TokenResponse.java` | Access Token 응답 DTO |
| `auth/dto/QrTokenResponse.java` | QR 토큰 응답 DTO |
| `auth/dto/AuthTokens.java` | Access + Refresh 내부 전달 DTO |

### 참조용 생성 (4 files — Unit 2에서 확장)
| 파일 | 설명 |
|------|------|
| `store/domain/Store.java` | Store 엔티티 (최소 정의) |
| `store/domain/StoreTable.java` | StoreTable 엔티티 (최소 정의) |
| `store/repository/StoreRepository.java` | Store JPA Repository |
| `store/repository/StoreTableRepository.java` | StoreTable JPA Repository |

### 수정 (4 files)
| 파일 | 변경 내용 |
|------|----------|
| `common/config/JwtTokenProvider.java` | createAccessToken/createRefreshToken/createQrToken 분리 |
| `common/config/JwtAuthenticationFilter.java` | tokenType=ACCESS만 인증 |
| `common/config/SecurityConfig.java` | /api/auth/refresh permitAll 추가 |
| `resources/application.yml` | JWT access/refresh/qr expiration 분리 |

### DB 스키마 변경 (1 file)
| 파일 | 변경 내용 |
|------|----------|
| `resources/db/init.sql` | admins 테이블에 login_attempts, locked_until 추가 |

### 테스트 (3 files, 32 test cases)
| 파일 | 테스트 수 |
|------|----------|
| `auth/domain/AdminTest.java` | 5 tests |
| `auth/service/AuthServiceTest.java` | 17 tests |
| `common/config/JwtTokenProviderTest.java` | 5 tests + 2 추가 |

## API 엔드포인트
| Method | Path | Auth | 설명 |
|--------|------|------|------|
| POST | `/api/admin/auth/login` | 불필요 | 관리자 로그인 |
| POST | `/api/admin/auth/logout` | 불필요 | 로그아웃 (Cookie 삭제) |
| POST | `/api/customer/auth/login` | 불필요 | 테이블 인증 |
| POST | `/api/customer/auth/qr` | 불필요 | QR 코드 인증 |
| POST | `/api/auth/refresh` | Cookie | 토큰 갱신 (Rotation) |
| POST | `/api/admin/tables/qr-token` | ADMIN | QR 토큰 생성 |
