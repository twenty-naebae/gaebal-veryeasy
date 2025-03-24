package com.gaebal_easy.delivery.application.feign;

import com.gaebal_easy.client.hub.domain.entity.Hub;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HubLocationDto {
    private double latitude;
    private double longitude;

}
