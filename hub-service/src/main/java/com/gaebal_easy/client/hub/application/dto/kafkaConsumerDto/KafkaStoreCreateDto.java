package com.gaebal_easy.client.hub.application.dto.kafkaConsumerDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KafkaStoreCreateDto {
    private UUID id;
    private String address;
}
