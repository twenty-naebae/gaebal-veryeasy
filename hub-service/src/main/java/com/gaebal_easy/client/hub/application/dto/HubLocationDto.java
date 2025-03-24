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
public class HubLocationDto {
    private double latitude;
    private double longitude;
    public static HubLocationDto of(Hub hub) {
        return HubLocationDto.builder()
                .latitude(hub.getHubLocation().getLatitude())
                .longitude(hub.getHubLocation().getLongitude())
                .build();
    }

}
