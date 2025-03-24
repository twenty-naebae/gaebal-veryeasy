package gaebal_easy.common.global.exception;

public class OrderFailExceiption extends BaseException{
    public OrderFailExceiption(Code errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
