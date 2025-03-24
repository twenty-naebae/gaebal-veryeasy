package com.gaebal_easy.client.hub.presentation.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class HubManagerUpdateRequest {
    private Long userId;    // 업데이트 대상
    private String name;    // 변경할 이름
    private UUID hubId;     // 변경할 허브 ID
}
