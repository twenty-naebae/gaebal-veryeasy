package gaebal_easy.common.global.exception;

public class AccessDeniedException extends BaseException {
    public AccessDeniedException(String message) {
        super(Code.ACCESS_DENIED_EXCEPTION,message);
    }
}
