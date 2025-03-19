package com.gaebal_easy.client.user.application.dto;

import lombok.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class HubManagerInfoMessage {

    private Long userId;
    private String name;
    private String group;

    public static HubManagerInfoMessage of(Long userId, String name, String group) {
        return HubManagerInfoMessage.builder()
                .userId(userId)
                .name(name)
                .group(group)
                .build();
    }
}
