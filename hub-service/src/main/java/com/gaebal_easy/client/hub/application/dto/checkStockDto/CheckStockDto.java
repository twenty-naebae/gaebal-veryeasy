package com.gaebal_easy.client.hub.application.dto.checkStockDto;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CheckStockDto {
    private List<CheckStokProductDto> products;
    private UUID hubId;
    private UUID orderId;
}
