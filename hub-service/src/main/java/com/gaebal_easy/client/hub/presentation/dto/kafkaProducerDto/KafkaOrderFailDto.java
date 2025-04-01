package com.gaebal_easy.client.hub.presentation.dto.kafkaProducerDto;

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
