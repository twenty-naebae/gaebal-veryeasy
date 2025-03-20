package com.gaebal_easy.hub.presentation.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
