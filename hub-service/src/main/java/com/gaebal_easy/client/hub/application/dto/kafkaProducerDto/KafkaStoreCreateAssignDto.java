package com.gaebal_easy.client.hub.application.dto.kafkaProducerDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KafkaStoreCreateAssignDto {
    private UUID hubId;
    private UUID storeId;
}




