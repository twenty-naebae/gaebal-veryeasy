package com.gaebal_easy.delivery.application.service.consumer;

import com.gaebal_easy.delivery.application.dto.kafkaConsumerDto.KafkaRequireAddressToHubDto;
import com.gaebal_easy.delivery.application.feign.HubClient;
import com.gaebal_easy.delivery.application.feign.HubDirectDto;
import com.gaebal_easy.delivery.application.feign.HubRouteDto;
import com.gaebal_easy.delivery.application.service.DeliveryDetailService;
import com.gaebal_easy.delivery.domain.entity.Delivery;
import com.gaebal_easy.delivery.domain.entity.DeliveryDetail;
import com.gaebal_easy.delivery.domain.repository.DeliveryDetailRepository;
import com.gaebal_easy.delivery.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubRequireAddressConsumer {

    private final HubClient hubClient;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryDetailRepository deliveryDetailRepository;
    private final DeliveryDetailService deliveryDetailService;


    @KafkaListener(topics="hub_require_address", groupId = "delivery_group",containerFactory = "requireAddressToHubKafkaListenerContainerFactory")
    public void hubRequireAddress(KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto) {
        // 여기에서 받은 값들을 토대로 feignclient 요청을 보낸 후 delivery와 detailDelivery에 저장해줘야 할듯...
        HubRouteDto hubRouteDto = hubClient.getRoute(kafkaRequireAddressToHubDto.getSupplyStoreHubName(), kafkaRequireAddressToHubDto.getReceiptStoreHubName());

        // TODO : deliveryPersonId를 받아와야함.

        // delivery에 위에서 받아온 내용 저장!
        Delivery delivery = Delivery.of(hubRouteDto,kafkaRequireAddressToHubDto);
        deliveryRepository.save(delivery);

        // depart와 arrive가 같은 경우는 처리하지 않음
        // hubRouteDto 값을 토대로 다시 feignclient 요청을 보내야함
        // TODO : hubDeliveryPersonId를 받아와야함
        for(int i=0; i<hubRouteDto.getVisitHubName().size()-1; i++){
            // 여기에 시퀀스 넣어줘야함
            // 요청할 때, 0번째, 1번째 같이 보내줘야함
            String depart = hubRouteDto.getVisitHubName().get(i);
            String arrive = hubRouteDto.getVisitHubName().get(i+1);
            HubDirectDto realHubDirectDto = deliveryDetailService.getDirectHub(depart,arrive);
            HubDirectDto expectedHubDirectDto = hubClient.getDirectHub(depart,arrive);
            DeliveryDetail deliveryDetail = DeliveryDetail.of(realHubDirectDto,expectedHubDirectDto,i+1, kafkaRequireAddressToHubDto.getOrderId());
            deliveryDetailRepository.save(deliveryDetail);
        }
        // TODO : 이후 작업은, 카프카로 slack에 이벤트 발행!
    }
}
