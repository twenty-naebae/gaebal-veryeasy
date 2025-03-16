package gaebal_easy.common.global.exception.userService;

import gaebal_easy.common.global.exception.BaseException;
import gaebal_easy.common.global.exception.Code;

public class RequiredArgumentException extends BaseException {
    public RequiredArgumentException(String message) {
        super(Code.REQUIRED_ARGUMENT_EXCEPTION, Code.REQUIRED_ARGUMENT_EXCEPTION.getMessage()+" : " + message);
    }
}