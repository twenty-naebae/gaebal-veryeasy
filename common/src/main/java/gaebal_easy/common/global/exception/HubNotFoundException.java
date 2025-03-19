package gaebal_easy.common.global.exception;

public class HubNotFoundException extends BaseException {
    public HubNotFoundException(Code code) {
        super(code, code.getMessage());
    }
}
