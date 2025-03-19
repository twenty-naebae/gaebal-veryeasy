package com.gaebal_easy.client.hub.presentation.dto;

import org.springframework.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HubRequestDto {
    @Nullable
    private String name;

    @Nullable
    private String address;

    @Nullable
    private Double latitude;

    @Nullable
    private Double longitude;
}
