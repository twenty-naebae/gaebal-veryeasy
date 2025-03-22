package com.gaebal_easy.client.hub.application.dto;

import com.gaebal_easy.client.hub.domain.entity.VisitHub;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
