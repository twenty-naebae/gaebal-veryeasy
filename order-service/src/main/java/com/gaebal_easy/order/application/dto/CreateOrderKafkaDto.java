package com.gaebal_easy.order.application.dto;

import com.gaebal_easy.order.presentation.dto.ProductRequestDto;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderKafkaDto {

    private UUID orderId;
    private UUID receiverId;
    private UUID supplierId;
    private List<ProductRequestDto> products;
}
