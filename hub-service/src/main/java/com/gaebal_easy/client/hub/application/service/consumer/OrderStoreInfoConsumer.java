package com.gaebal_easy.client.hub.application.service.consumer;

import com.gaebal_easy.client.hub.application.dto.kafkaConsumerDto.KafkaOrderStoreInfoDto;
import com.gaebal_easy.client.hub.application.dto.kafkaConsumerDto.StoreInfoKafkaDTO;
import com.gaebal_easy.client.hub.application.dto.kafkaProducerDto.KafkaRequireAddressToHubDto;
import com.gaebal_easy.client.hub.application.service.producer.HubRequireAddressProducer;
import com.gaebal_easy.client.hub.application.service.producer.OrderFailProducer;
import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.entity.HubProductList;
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
    private final OrderFailProducer orderFailProducer;
    private final HubRequireAddressProducer hubRequireAddressProducer;

    @KafkaListener(topics = "store-order-info", groupId = "hub_group", containerFactory = "orderStoreInfoKafkaListenerContainerFactory")
    public void orderStoreInfo(StoreInfoKafkaDTO storeInfoKafkaDTO) {
        Hub supplier = hubRepository.getHub(storeInfoKafkaDTO.getSupplierHubId()).orElseThrow(() -> new NullPointerException("허브가 없습니다."));
        Hub receiver = hubRepository.getHub(storeInfoKafkaDTO.getReceiverHubId()).orElseThrow(() -> new NullPointerException("허브가 없습니다."));
        KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto = KafkaRequireAddressToHubDto.of(supplier, receiver, storeInfoKafkaDTO);
        hubRequireAddressProducer.sendRequireAddressEvent(kafkaRequireAddressToHubDto);

    }
}
