package gaebal_easy.common.global.exception;

public class ProductNotFoundException extends BaseException {
    public ProductNotFoundException(Code code) {
        super(code, code.getMessage());
    }
}
