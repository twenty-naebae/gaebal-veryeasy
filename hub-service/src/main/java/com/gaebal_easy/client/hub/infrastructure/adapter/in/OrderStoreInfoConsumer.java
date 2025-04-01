package com.gaebal_easy.client.hub.infrastructure.adapter.in;

import com.gaebal_easy.client.hub.presentation.dto.kafkaConsumerDto.StoreInfoKafkaDTO;
import com.gaebal_easy.client.hub.presentation.dto.kafkaProducerDto.KafkaRequireAddressToHubDto;
import com.gaebal_easy.client.hub.infrastructure.adapter.out.HubRequireAddressProducer;
import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.repository.HubProductListRepository;
import com.gaebal_easy.client.hub.domain.repository.HubRepository;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.HubNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderStoreInfoConsumer {
    private final HubRepository hubRepository;
    private final HubProductListRepository hubProductListRepository;
    private final HubRequireAddressProducer hubRequireAddressProducer;

    @KafkaListener(topics = "store-order-info", groupId = "hub_group", containerFactory = "orderStoreInfoKafkaListenerContainerFactory")
    public void orderStoreInfo(StoreInfoKafkaDTO storeInfoKafkaDTO) {
        Hub supplier = hubRepository.getHub(storeInfoKafkaDTO.getSupplierHubId()).orElseThrow(() -> new HubNotFoundException(Code.HUB_NOT_FOUND));
        Hub receiver = hubRepository.getHub(storeInfoKafkaDTO.getReceiverHubId()).orElseThrow(() -> new HubNotFoundException(Code.HUB_NOT_FOUND));
        KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto = KafkaRequireAddressToHubDto.of(supplier, receiver, storeInfoKafkaDTO);
        hubRequireAddressProducer.sendRequireAddressEvent(kafkaRequireAddressToHubDto);

    }
}
