package gaebal_easy.common.global.exception;

public class CanNotFindTokenException extends BaseException {
    public CanNotFindTokenException(Code code) {
        super(code, Code.USER_CAN_NOT_FIND_TOKEN.getMessage());
    }
}