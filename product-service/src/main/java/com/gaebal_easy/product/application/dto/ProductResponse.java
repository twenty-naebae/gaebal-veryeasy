package com.gaebal_easy.product.application.dto;


import com.gaebal_easy.product.domain.entity.Product;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class ProductResponse {

    private UUID id;
    private String name;
    private Long price;
    private String description;

    public static ProductResponse of(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();
    }


}
