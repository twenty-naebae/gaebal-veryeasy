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
    private UUID supplier;
    private UUID receiver;
    private String orderRequest;
    private Long totalPrice;
    private String address;

    public static OrderResponse from(Order order){
        return OrderResponse.builder()
                .id(order.getId())
                .supplier(order.getSupplierId())
                .receiver(order.getReceiverId())
                .orderRequest(order.getOrderRequest())
                .totalPrice(order.getTotalPrice())
                .address(order.getAddress())
                .build();
    }

}
