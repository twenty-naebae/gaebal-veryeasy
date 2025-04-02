package com.gaebal_easy.delivery.application.dto;

import com.gaebal_easy.delivery.infrastructure.dto.kafkaConsumerDto.ProductRequestDto;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RequireAddressToHubServiceDto {
    private UUID orderId;
    private UUID arriveHubId;
    private String supplyStoreHubName;
    private String receiptStoreHubName;
    private List<ProductRequestDto> products;
    private String receiptStoreAddress;
    private String supplyStoreName;
    private String receiptStoreName;
}
