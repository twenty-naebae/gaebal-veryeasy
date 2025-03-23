package gaebal_easy.common.global.message;

import gaebal_easy.common.global.dto.BaseMessage;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC) // 직렬화할때 기본생성자 필요함.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class DeliveryUserDeleteMessage extends BaseMessage {

    private Long userId;
    private String deletedBy;

    public static DeliveryUserDeleteMessage of(Long userId, String deletedBy) {
        return DeliveryUserDeleteMessage.builder()
                .userId(userId)
                .deletedBy(deletedBy)
                .build();
    }
}
