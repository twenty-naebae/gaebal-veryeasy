package com.gaebal_easy.delivery.application.feign;

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
}
