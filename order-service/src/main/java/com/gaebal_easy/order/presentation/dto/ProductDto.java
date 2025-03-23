package com.gaebal_easy.order.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductDto {

    private UUID productId;
    private Long quantity;
    private Long price;
}
