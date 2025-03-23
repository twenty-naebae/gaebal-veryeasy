package com.gaebal_easy.delivery.application.dto.kafkaConsumerDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KafkaRequireAddressToHubDto {
    private UUID orderId;
    private String supplyStoreHubName;
    private String receiptStoreHubName;
    private String orderer;
    private String productName;
    private String receiptStoreAddress;
    private String supplyStoreName;
    private String receiptStoreName;
}
