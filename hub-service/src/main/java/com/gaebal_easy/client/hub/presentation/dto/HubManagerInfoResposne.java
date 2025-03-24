package com.gaebal_easy.client.hub.presentation.dto;

import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class HubManagerInfoResposne {

    private UUID hubManagerId;
    private Long userId;
    private String name;
    private UUID hubId;

    public static HubManagerInfoResposne of(UUID hubManagerId, Long userId, String name, UUID hubId) {
        return HubManagerInfoResposne.builder()
                .hubManagerId(hubManagerId)
                .userId(userId)
                .name(name)
                .hubId(hubId)
                .build();
    }
}
