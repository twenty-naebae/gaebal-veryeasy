package com.gaebal_easy.client.hub.application.dto;

import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.entity.HubProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductResponseDto {
    private String ProductName;
    private String description;
    private Long price;
    private Long amount;
    private String hubName;

    public static ProductResponseDto of(HubProduct hubProductList, Hub hub) {
        return ProductResponseDto.builder()
                .ProductName(hubProductList.getName())
                .amount(hubProductList.getAmount())
                .description(hubProductList.getDescription())
                .price(hubProductList.getPrice())
                .hubName(hub.getHubLocation().getName())
                .build();
    }
}
