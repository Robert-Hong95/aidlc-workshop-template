package com.tableorder.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // Common
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "잘못된 입력입니다"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다"),

    // Auth
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "인증 정보가 올바르지 않습니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    LOGIN_ATTEMPTS_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "로그인 시도 횟수를 초과했습니다"),

    DUPLICATE_ADMIN(HttpStatus.CONFLICT, "이미 존재하는 관리자입니다"),

    // Store
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "매장을 찾을 수 없습니다"),
    DUPLICATE_STORE_CODE(HttpStatus.CONFLICT, "이미 존재하는 매장 코드입니다"),

    // Table
    TABLE_NOT_FOUND(HttpStatus.NOT_FOUND, "테이블을 찾을 수 없습니다"),
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "활성 세션을 찾을 수 없습니다"),
    DUPLICATE_TABLE_NO(HttpStatus.CONFLICT, "이미 존재하는 테이블 번호입니다"),

    // Menu
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다"),
    CATEGORY_HAS_MENUS(HttpStatus.CONFLICT, "메뉴가 존재하는 카테고리는 삭제할 수 없습니다"),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, "유효하지 않은 가격입니다"),

    // Order
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다"),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 주문 상태 변경입니다"),
    HAS_INCOMPLETE_ORDERS(HttpStatus.CONFLICT, "미완료 주문이 존재하여 세션을 종료할 수 없습니다"),

    // File
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다"),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
}
