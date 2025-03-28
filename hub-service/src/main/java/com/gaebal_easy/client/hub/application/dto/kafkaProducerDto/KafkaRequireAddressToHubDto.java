package com.gaebal_easy.client.hub.application.dto.kafkaProducerDto;

import com.gaebal_easy.client.hub.application.dto.kafkaConsumerDto.ProductRequestDto;
import com.gaebal_easy.client.hub.application.dto.kafkaConsumerDto.StoreInfoKafkaDTO;
import com.gaebal_easy.client.hub.domain.entity.Hub;
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
    private String supplyStoreHubName;
    private String receiptStoreHubName;
    private UUID arriveHubId;
    private List<ProductRequestDto> products;
    private String receiptStoreAddress;
    private String supplyStoreName;
    private String receiptStoreName;

    public static KafkaRequireAddressToHubDto of(Hub supplier, Hub receiver, StoreInfoKafkaDTO storeInfoKafkaDTO){
        return KafkaRequireAddressToHubDto.builder()
                .orderId(storeInfoKafkaDTO.getOrderId())
                .supplyStoreHubName(supplier.getHubLocation().getName())
                .receiptStoreHubName(receiver.getHubLocation().getName())
                .products(storeInfoKafkaDTO.getProducts())
                .receiptStoreAddress(storeInfoKafkaDTO.getReceiverAddress())
                .supplyStoreName(storeInfoKafkaDTO.getSupplierName())
                .receiptStoreName(storeInfoKafkaDTO.getReceiverName())
                .arriveHubId(receiver.getId())
                .build();
    }
}
