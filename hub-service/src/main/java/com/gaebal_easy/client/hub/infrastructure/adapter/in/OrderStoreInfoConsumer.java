package com.gaebal_easy.client.hub.infrastructure.adapter.in;

import com.gaebal_easy.client.hub.presentation.dto.kafkaConsumerDto.StoreInfoKafkaDTO;
import com.gaebal_easy.client.hub.presentation.dto.kafkaProducerDto.KafkaRequireAddressToHubDto;
import com.gaebal_easy.client.hub.infrastructure.adapter.out.HubRequireAddressProducer;
import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.repository.HubProductListRepository;
import com.gaebal_easy.client.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderStoreInfoConsumer {
    private final HubRepository hubRepository;
    private final HubProductListRepository hubProductListRepository;
    private final HubRequireAddressProducer hubRequireAddressProducer;

    @KafkaListener(topics = "store-order-info", groupId = "hub_group", containerFactory = "orderStoreInfoKafkaListenerContainerFactory")
    public void orderStoreInfo(StoreInfoKafkaDTO storeInfoKafkaDTO) {
        Hub supplier = hubRepository.getHub(storeInfoKafkaDTO.getSupplierHubId()).orElseThrow(() -> new NullPointerException("허브가 없습니다."));
        Hub receiver = hubRepository.getHub(storeInfoKafkaDTO.getReceiverHubId()).orElseThrow(() -> new NullPointerException("허브가 없습니다."));
        KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto = KafkaRequireAddressToHubDto.of(supplier, receiver, storeInfoKafkaDTO);
        hubRequireAddressProducer.sendRequireAddressEvent(kafkaRequireAddressToHubDto);

    }
}
