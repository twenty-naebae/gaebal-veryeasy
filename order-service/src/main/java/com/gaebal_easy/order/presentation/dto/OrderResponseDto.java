package com.gaebal_easy.order.presentation.dto;

import com.gaebal_easy.order.application.dto.OrderResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderResponseDto {

    private UUID id;
    private String supplier;
    private String receiver;
    private String orderRequest;
    private Long totalPrice;

    public static OrderResponseDto from(OrderResponse orderResponse) {
        return OrderResponseDto.builder()
                .id(orderResponse.getId())
                .supplier(orderResponse.getSupplier())
                .receiver(orderResponse.getReceiver())
                .orderRequest(orderResponse.getOrderRequest())
                .totalPrice(orderResponse.getTotalPrice())
                .build();
    }

}
