package com.mysite.sbb.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
    INACTIVE_USER(HttpStatus.FORBIDDEN, "User is inactive"),
    USER_NOT_FOUND_BY_EMAIL(HttpStatus.NOT_FOUND, "해당 이메일의 유저가 없습니다"),
    USER_NOT_FOUND_BY_USERNAME(HttpStatus.NOT_FOUND, "해당 ID의 유저가 없습니다"),
    CREATE_MAIL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 생성 에러"),
    SEND_MAIL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송 에러"),
    SIGN_UP_FAIL(HttpStatus.BAD_REQUEST, "이미 등록된 사용자입니다")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}