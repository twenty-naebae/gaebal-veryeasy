package com.gaebal_easy.client.hub.presentation.dto;

import gaebal_easy.common.global.dto.BaseMessage;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC) // 직렬화할때 기본생성자 필요함.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class HubManagerInfoMessage extends BaseMessage {

    private Long userId;
    private String name;
    private String group;

    public static HubManagerInfoMessage of(Long userId, String name, String group, String errorType) {
        return HubManagerInfoMessage.builder()
                .userId(userId)
                .name(name)
                .group(group)
                .build();
    }
}
