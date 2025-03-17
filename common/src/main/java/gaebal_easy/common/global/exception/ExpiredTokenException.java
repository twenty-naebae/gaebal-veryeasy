package gaebal_easy.common.global.exception;

public class ExpiredTokenException extends BaseException {
    public ExpiredTokenException(Code code) {
        super(code, code.getMessage());
    }
}