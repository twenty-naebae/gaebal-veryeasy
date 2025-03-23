package com.gaebal_easy.client.hub.application.service.consumer;

import com.gaebal_easy.client.hub.application.dto.kafkaConsumerDto.KafkaOrderStoreInfoDto;
import com.gaebal_easy.client.hub.application.dto.kafkaProducerDto.KafkaRequireAddressToHubDto;
import com.gaebal_easy.client.hub.application.service.producer.HubRequireAddressProducer;
import com.gaebal_easy.client.hub.application.service.producer.OrderFailProducer;
import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.entity.HubProduct;
import com.gaebal_easy.client.hub.domain.repository.HubProductListRepository;
import com.gaebal_easy.client.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderStoreInfoConsumer {
    private final HubRepository hubRepository;
    private final HubProductListRepository hubProductListRepository;
    private final OrderFailProducer orderFailProducer;
    private final HubRequireAddressProducer hubRequireAddressProducer;

    @KafkaListener(topics="order_store_info", groupId = "hub_group",containerFactory = "orderStoreInfoKafkaListenerContainerFactory")
    public void orderStoreInfo(KafkaOrderStoreInfoDto kafkaOrderStoreInfoDto) {

        long productAmount = kafkaOrderStoreInfoDto.getAmount();
        HubProduct hubProduct = hubProductListRepository.getProduct(kafkaOrderStoreInfoDto.getProductId()).orElseThrow();
        if(hubProduct.getAmount() < productAmount)
            orderFailProducer.sendOrderFailEvent(kafkaOrderStoreInfoDto.getOrderId(), "재고 부족");
        else{
            Hub supplier = hubRepository.getHub(kafkaOrderStoreInfoDto.getSupplyStoreHubId()).orElseThrow(()-> new NullPointerException("허브가 없습니다."));
            Hub receiver = hubRepository.getHub(kafkaOrderStoreInfoDto.getReceiptStoreHubId()).orElseThrow(()-> new NullPointerException("허브가 없습니다."));
            KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto = KafkaRequireAddressToHubDto.of(supplier, receiver, kafkaOrderStoreInfoDto);
            hubRequireAddressProducer.sendRequireAddressEvent(kafkaRequireAddressToHubDto);
        }
    }
}
