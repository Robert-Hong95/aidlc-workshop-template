# Domain Entities - Unit 5-FE (공통 레이아웃 + Auth UI)

> Frontend에서 사용하는 클라이언트 측 도메인 모델 정의

## 1. Auth 관련 타입

### AdminAuth (관리자 인증 상태)
| 필드 | 타입 | 설명 |
|------|------|------|
| accessToken | string | null | JWT Access Token |
| refreshToken | string | null | JWT Refresh Token |
| storeId | number | null | 매장 ID |
| storeName | string | null | 매장명 |
| adminId | number | null | 관리자 ID |
| username | string | null | 사용자명 |
| isAuthenticated | boolean | 인증 여부 |

### TableAuth (테이블 인증 상태)
| 필드 | 타입 | 설명 |
|------|------|------|
| accessToken | string | null | JWT Access Token |
| refreshToken | string | null | JWT Refresh Token |
| storeId | number | null | 매장 ID |
| storeName | string | null | 매장명 |
| storeCode | string | null | 매장 코드 |
| tableId | number | null | 테이블 ID |
| tableNo | number | null | 테이블 번호 |
| isAuthenticated | boolean | 인증 여부 |

## 2. API 요청/응답 타입

### LoginRequest
| 필드 | 타입 | 설명 |
|------|------|------|
| storeCode | string | 매장 코드 |
| username | string | 사용자명 |
| password | string | 비밀번호 |

### TableLoginRequest
| 필드 | 타입 | 설명 |
|------|------|------|
| storeCode | string | 매장 코드 |
| tableNo | number | 테이블 번호 |
| password | string | 비밀번호 |

### TokenResponse
| 필드 | 타입 | 설명 |
|------|------|------|
| accessToken | string | JWT Access Token (1시간) |
| refreshToken | string | JWT Refresh Token (16시간) |
| storeId | number | 매장 ID |
| storeName | string | 매장명 |

### AdminTokenResponse (extends TokenResponse)
| 필드 | 타입 | 설명 |
|------|------|------|
| adminId | number | 관리자 ID |
| username | string | 사용자명 |

### TableTokenResponse (extends TokenResponse)
| 필드 | 타입 | 설명 |
|------|------|------|
| tableId | number | 테이블 ID |
| tableNo | number | 테이블 번호 |

## 3. localStorage 키 매핑

### 관리자앱
| 키 | 값 | 용도 |
|---|---|------|
| `admin_access_token` | string | Access Token |
| `admin_refresh_token` | string | Refresh Token |
| `admin_store_id` | number | 매장 ID |
| `admin_store_name` | string | 매장명 |
| `admin_id` | number | 관리자 ID |
| `admin_username` | string | 사용자명 |
| `admin_login_attempts` | number | 로그인 실패 횟수 |
| `admin_login_last_fail` | string (ISO) | 마지막 실패 시각 |
| `admin_sidebar_collapsed` | boolean | 사이드바 접힘 상태 |

### 고객앱
| 키 | 값 | 용도 |
|---|---|------|
| `table_access_token` | string | Access Token |
| `table_refresh_token` | string | Refresh Token |
| `table_store_id` | number | 매장 ID |
| `table_store_name` | string | 매장명 |
| `table_store_code` | string | 매장 코드 |
| `table_id` | number | 테이블 ID |
| `table_no` | number | 테이블 번호 |
