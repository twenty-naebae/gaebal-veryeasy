package com.gaebal_easy.delivery.application.feign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HubLocationDto {
    private double latitude;
    private double longitude;

}
