package com.gaebal_easy.order.application.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateKafkaDto {

    private UUID orderId;
    private String receiver;
    private String supplier;
}
