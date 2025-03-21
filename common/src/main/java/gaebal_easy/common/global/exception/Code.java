package gaebal_easy.common.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum Code {

    /**
     * 공통 코드
     */
    SUCCESS(HttpStatus.OK, 0, "성공적으로 처리되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,1,"서버에러"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, 2, "잘못된 입력값이 존재합니다."),
    ACCESS_DENIED_EXCEPTION(HttpStatus.FORBIDDEN, 3, "권한이 없습니다."),
    /**
     * Gate-way 1000번대
     */
    GATEWAY_REQUIRED_ARGUMENT_EXCEPTION(HttpStatus.BAD_REQUEST, 1001, "입력값이 없는 항목이 있습니다."),
    GATEWAY_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 1002, "토큰이 만료되었습니다."),
    GATEWAY_CAN_NOT_FIND_TOKEN(HttpStatus.BAD_REQUEST, 1003, "해당 토큰을 찾을 수 없습니다."),
    GATEWAY_REQUIRED_LOGIN(HttpStatus.UNAUTHORIZED, 1004, "로그인이 필요합니다."),
    GATEWAY_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 1005, "유효하지 않은 토큰입니다."),
    /**
     * Eureak 2000번대
     */

    /**
     * User 3000번대
     */
    USER_REQUIRED_ARGUMENT_EXCEPTION(HttpStatus.BAD_REQUEST, 3001, "입력값이 없는 항목이 있습니다."),
    USER_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 3002, "토큰이 만료되었습니다."),
    USER_CAN_NOT_FIND_TOKEN(HttpStatus.BAD_REQUEST, 3003, "해당 토큰을 찾을 수 없습니다."),
    CAN_NOT_FIND_USER(HttpStatus.BAD_REQUEST,3004 , "해당 유저를 찾을 수 없습니다." ),

    /**
     * Hub 4000번대
     */
    HUB_NOT_FOUND(HttpStatus.BAD_REQUEST,4001,"허브가 존재하지 않습니다."),
    HUB_PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST,4002,"상품이 존재하지 않습니다."),
    HUB_CAN_NOT_FIND_PRODUCT_IN_HUB(HttpStatus.BAD_REQUEST,4003,"허브에 해당 상품이 존재하지 않습니다."),

    /**
     * Delivery 5000번대
     */

    /**
     * Product 6000번대
     */

    /**
     * Order 7000번대
     */

    /**
     * Store 8000번대
     */

    /**
     * Slack 9000번대
     */
    SLACK_MESSAGE_NOT_SEND_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, 9001, "슬랙 메시지 전송이 실패하였습니다."),
    SLACK_INVALID_USER_ID_EXCEPTION(HttpStatus.BAD_REQUEST, 9002, "유효하지 않은 Slack User ID입니다."),
    SLACK_AUTH_EXCEPTION(HttpStatus.UNAUTHORIZED, 9003, "Slack 인증에 실패하였습니다."),
    SLACK_RATE_LIMITED_EXCEPTION(HttpStatus.TOO_MANY_REQUESTS, 9004, "Slack API 요청이 너무 많습니다. 잠시 후 다시 시도해주세요."),
    SLACK_DM_CHANNEL_OPEN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 9005, "DM 채널을 열 수 없습니다."),
    GEMINI_PARSING_EXCEPTION(HttpStatus.NOT_FOUND, 9006, "발송 데드라인 생성 중 오류가 발생했습니다."),
    SLACK_MESSAGE_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, 9007, "슬랙메시지가 존재하지 않습니다.");

    /**
     * Kafka 통신 10000번대
     */

    /**
     * feighn Client 통신 11000번대
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