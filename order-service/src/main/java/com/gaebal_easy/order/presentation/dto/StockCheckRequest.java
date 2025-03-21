package com.gaebal_easy.order.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class StockCheckRequest {

    private List<ProductDto> products;
    private UUID hubId;
}
