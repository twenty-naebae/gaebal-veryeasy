package gaebal_easy.common.global.exception.userService;

import gaebal_easy.common.global.exception.BaseException;
import gaebal_easy.common.global.exception.Code;

public class ExpiredTokenException extends BaseException {
    public ExpiredTokenException() {
        super(Code.EXPIRED_TOKEN, Code.EXPIRED_TOKEN.getMessage());
    }
}