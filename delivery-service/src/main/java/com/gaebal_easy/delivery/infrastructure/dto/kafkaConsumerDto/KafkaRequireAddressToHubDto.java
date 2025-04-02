package com.gaebal_easy.delivery.infrastructure.dto.kafkaConsumerDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class KafkaRequireAddressToHubDto {
    private UUID orderId;
    private UUID arriveHubId;
    private String supplyStoreHubName;
    private String receiptStoreHubName;
    private List<ProductRequestDto> products;
    private String receiptStoreAddress;
    private String supplyStoreName;
    private String receiptStoreName;
}
