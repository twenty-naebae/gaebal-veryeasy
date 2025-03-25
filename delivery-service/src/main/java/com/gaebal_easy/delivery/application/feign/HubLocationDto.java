package com.gaebal_easy.delivery.application.feign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HubLocationDto {
    private double latitude;
    private double longitude;

}
