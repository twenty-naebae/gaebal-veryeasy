package com.gaebal_easy.client.hub.application.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class StockCheckDto {
    private List<CheckStokProductDto> products;
    private UUID hubId;
}
