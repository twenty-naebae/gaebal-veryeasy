package com.gaebal_easy.client.hub.application.dto.kafkaProducerDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KafkaOrderFailDto {
    private UUID orderId;
    private String message;
}
