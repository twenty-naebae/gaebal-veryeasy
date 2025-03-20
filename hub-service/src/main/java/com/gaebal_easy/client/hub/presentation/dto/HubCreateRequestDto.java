package com.gaebal_easy.client.hub.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HubCreateRequestDto {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
}
