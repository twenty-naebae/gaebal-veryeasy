package gaebal_easy.common.global.message;

import gaebal_easy.common.global.dto.BaseMessage;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC) // 직렬화할때 기본생성자 필요함.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class HubManagerUpdateMessage extends BaseMessage {
    private Long userId;
    private String name;
    private String group;

    public static HubManagerUpdateMessage of(Long userId, String name, String group) {
        return HubManagerUpdateMessage.builder()
                .userId(userId)
                .name(name)
                .group(group)
                .build();
    }

}
