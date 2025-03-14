package com.gaebal_easy.client.user.global.exception;

import static com.gaebal_easy.client.user.global.exception.Code.INTERNAL_SERVER_ERROR;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final Code errorCode;

    // 에러 메시지를 받는 생성자
    public BaseException(String message) {
        super(message);
        this.errorCode = INTERNAL_SERVER_ERROR;
    }

    // 에러 코드를 지정하는 생성자
    public BaseException(Code errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 에러 코드와 메시지를 받는 생성자
    public BaseException(Code errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}