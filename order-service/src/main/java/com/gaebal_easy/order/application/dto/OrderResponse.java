package com.gaebal_easy.order.application.dto;


import com.gaebal_easy.order.domain.entity.Order;
import lombok.*;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderResponse {

    private UUID id;
    private String supplier;
    private String receiver;
    private String orderRequest;
    private Long totalPrice;

    public static OrderResponse from(Order order){
        return OrderResponse.builder()
                .id(order.getId())
                .supplier(order.getSupplier())
                .receiver(order.getReceiver())
                .orderRequest(order.getOrderRequest())
                .totalPrice(order.getTotalPrice())
                .build();
    }

}
