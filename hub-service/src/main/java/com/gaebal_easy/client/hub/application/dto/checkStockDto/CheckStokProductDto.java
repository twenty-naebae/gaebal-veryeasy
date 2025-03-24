package com.gaebal_easy.client.hub.application.dto.checkStockDto;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckStokProductDto implements Serializable {

    private UUID productId;
    private Long quantity;
    private Long price;
}
