package com.gaebal_easy.client.hub.application.dto.kafkaProducerDto;

import com.gaebal_easy.client.hub.application.dto.kafkaConsumerDto.KafkaOrderStoreInfoDto;
import com.gaebal_easy.client.hub.domain.entity.Hub;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class KafkaRequireAddressToHubDto {
    private UUID orderId;
    private String supplyStoreHubName;
    private String receiptStoreHubName;
    private String orderer;
    private String productName;
    private String receiptStoreAddress;
    private String supplyStoreName;
    private String receiptStoreName;

    public static KafkaRequireAddressToHubDto of(Hub supplier, Hub receiver, KafkaOrderStoreInfoDto kafkaOrderStoreInfoDto){
        return KafkaRequireAddressToHubDto.builder()
                .orderId(kafkaOrderStoreInfoDto.getOrderId())
                .supplyStoreHubName(supplier.getHubLocation().getName())
                .receiptStoreHubName(receiver.getHubLocation().getName())
                .orderer(kafkaOrderStoreInfoDto.getOrderer())
                .productName(kafkaOrderStoreInfoDto.getProductName())
                .receiptStoreAddress(receiver.getHubLocation().getAddress())
                .supplyStoreName(kafkaOrderStoreInfoDto.getSupplyStoreName())
                .receiptStoreName(kafkaOrderStoreInfoDto.getReceiptStoreName())
                .build();
    }
}
