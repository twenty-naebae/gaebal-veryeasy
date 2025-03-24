package com.gaebal_easy.delivery.presentation.dto;

import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DeliveryUserInfoResponse {
    private UUID id;
    private Long userId;
    private String name;
    private String slackId;
    private UUID hubId; // hub배송담당자는 null

    public static DeliveryUserInfoResponse of(UUID id, Long userId, String name, String slackId, UUID hubId) {
        return DeliveryUserInfoResponse.builder()
                .id(id)
                .userId(userId)
                .name(name)
                .slackId(slackId)
                .hubId(hubId)
                .build();
    }
}
