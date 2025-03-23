package com.gaebal_easy.client.hub.application.dto.kafkaConsumerDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KafkaOrderStoreInfoDto {
    private UUID orderId;
    private UUID productId;
    private UUID supplyStoreHubId;
    private String supplyStoreName;
    private UUID receiptStoreHubId;
    private String receiptStoreName;
    private String receiptStoreAddress;
    private String orderer;
    private String productName;
    private long amount;
}
