package gaebal_easy.common.global.exception.userService;

import gaebal_easy.common.global.exception.BaseException;
import gaebal_easy.common.global.exception.Code;

public class CanNotFindTokenException extends BaseException {
    public CanNotFindTokenException() {
        super(Code.CAN_NOT_FIND_TOKEN, Code.CAN_NOT_FIND_TOKEN.getMessage());
    }
}