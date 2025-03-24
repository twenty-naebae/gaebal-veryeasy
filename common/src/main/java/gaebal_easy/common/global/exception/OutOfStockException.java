package gaebal_easy.common.global.exception;

public class OutOfStockException extends BaseException{
    public OutOfStockException(Code errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
