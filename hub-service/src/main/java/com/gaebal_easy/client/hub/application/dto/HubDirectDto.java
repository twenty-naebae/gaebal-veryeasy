package com.gaebal_easy.client.hub.application.dto;

import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.entity.HubDirectMovementInfo;
import com.gaebal_easy.client.hub.domain.entity.HubProductList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class HubDirectDto {
    private String departName;
    private String arriveName;
    private int time;
    private double distance;

    public static HubDirectDto of(HubDirectMovementInfo hubDirectMovementInfo) {
        return HubDirectDto.builder()
                .departName(hubDirectMovementInfo.getDepart())
                .arriveName(hubDirectMovementInfo.getArrive())
                .time(hubDirectMovementInfo.getRequiredTime())
                .distance(hubDirectMovementInfo.getDistance())
                .build();
    }

}
