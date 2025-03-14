package com.gaebal_easy.client.user.global.exception;

import java.util.Optional;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Code {

    /**
     * 성공 0번대
     */
    SUCCESS(HttpStatus.OK, 0, "성공적으로 처리되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,1,"서버에러"),
    /**
     * Gate-way 10000번대
     */
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, 10000, "잘못된 입력값이 존재합니다.");

    /**
     * Eureak 20000번대
     */

    /**
     * User 30000번대
     */

    /**
     * Hub 40000번대
     */

    /**
     * Delivery 50000번대
     */

    /**
     * Product 60000번대
     */

    /**
     * Order 80000번대
     */

    /**
     * Store 90000번대
     */

    /**
     * Slcak 100000번대
     */

    /**
     * Kafka 통신 110000번대
     */

    /**
     * feighn Client 통신 120000번대
     */


    private final HttpStatus status;
    private final Integer code;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
        // 결과 예시 - "Validation error - Reason why it isn't valid"
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    public String getDetailMessage(String message) {
        return this.getMessage() + " : " + message;
    }
}