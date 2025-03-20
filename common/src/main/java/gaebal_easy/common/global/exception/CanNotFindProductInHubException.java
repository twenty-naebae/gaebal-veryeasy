package gaebal_easy.common.global.exception;

public class CanNotFindProductInHubException extends BaseException{
    public CanNotFindProductInHubException(Code code) {
        super(code, code.getMessage());
    }
}
