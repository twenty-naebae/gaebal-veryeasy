package gaebal_easy.common.global.message;

import gaebal_easy.common.global.dto.BaseMessage;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC) // 직렬화할때 기본생성자 필요함.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class DeliveryUserInfoMessage extends BaseMessage {

    private Long userId;
    private String slackId;
    private String name;
    private String group;

    public static DeliveryUserInfoMessage of(Long userId, String name, String group, String slackId) {
        return DeliveryUserInfoMessage.builder()
                .userId(userId)
                .name(name)
                .slackId(slackId)
                .group(group)
                .build();
    }
}

