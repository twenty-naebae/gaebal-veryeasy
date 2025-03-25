package gaebal_easy.common.global.exception;

public class OrderNotFoundException extends BaseException {

    public OrderNotFoundException(Code errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
