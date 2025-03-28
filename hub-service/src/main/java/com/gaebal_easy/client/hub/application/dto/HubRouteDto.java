package com.gaebal_easy.client.hub.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class HubRouteDto {
    private String depart;
    private String arrive;
    private int totalRequiredTime;
    private double totalDistance;
    private List<String> visitHubName;
}
