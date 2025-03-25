package gaebal_easy.common.global.exception;

public class CanNotFindHubNameException extends BaseException{
    public CanNotFindHubNameException(Code code) {
        super(code, code.getMessage());
    }
}
