package com.gaebal_easy.client.hub.application.dto;

import com.gaebal_easy.client.hub.domain.entity.Hub;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class HubResponseDto {
    private String name;
    private String address;
    public static HubResponseDto of(Hub hub) {
        return HubResponseDto.builder()
                .name(hub.getHubLocation().getName())
                .address(hub.getHubLocation().getAddress())
                .build();
    }
}
