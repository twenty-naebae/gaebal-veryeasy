package gaebal_easy.common.global.exception;

// 필요한 인자가 존재하지 않을때
public class RequiredArgumentException extends BaseException {
    public RequiredArgumentException(Code code, String message) {
        super(code, message);
    }
}