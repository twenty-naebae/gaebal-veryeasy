package com.gaebal_easy.delivery.application.service.consumer;

import com.gaebal_easy.delivery.application.dto.kafkaConsumerDto.KafkaRequireAddressToHubDto;
import com.gaebal_easy.delivery.application.dto.kafkaConsumerDto.ProductRequestDto;
import com.gaebal_easy.delivery.application.dto.kafkaProducerDto.SlackMessageInfoDTO;
import com.gaebal_easy.delivery.application.feign.HubClient;
import com.gaebal_easy.delivery.application.feign.HubDirectDto;
import com.gaebal_easy.delivery.application.feign.HubRouteDto;
import com.gaebal_easy.delivery.application.service.DeliveryDetailService;
import com.gaebal_easy.delivery.application.service.DeliveryUserAssignmentService;
import com.gaebal_easy.delivery.application.service.producer.SlackMessageProducer;
import com.gaebal_easy.delivery.domain.entity.Delivery;
import com.gaebal_easy.delivery.domain.entity.DeliveryDetail;
import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;
import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;
import com.gaebal_easy.delivery.domain.repository.DeliveryDetailRepository;
import com.gaebal_easy.delivery.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HubRequireAddressConsumer {

    private final HubClient hubClient;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryDetailRepository deliveryDetailRepository;
    private final DeliveryDetailService deliveryDetailService;
    private final DeliveryUserAssignmentService deliveryUserAssignmentService;
    private final SlackMessageProducer slackMessageProducer;


    @KafkaListener(topics="hub_require_address", groupId = "delivery_group",containerFactory = "requireAddressToHubKafkaListenerContainerFactory")
    public void hubRequireAddress(KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto) {
        SlackMessageInfoDTO slackMessageInfoDTO = new SlackMessageInfoDTO();

        HubRouteDto hubRouteDto = hubClient.getRoute(kafkaRequireAddressToHubDto.getSupplyStoreHubName().substring(0,4),
                kafkaRequireAddressToHubDto.getReceiptStoreHubName().substring(0,4));

        StoreDeliveryUser storeDeliveryUser = deliveryUserAssignmentService.assignStoreDeliveryUser(kafkaRequireAddressToHubDto.getArriveHubId());

        // delivery에 위에서 받아온 내용 저장!
        Delivery delivery = Delivery.of(hubRouteDto,kafkaRequireAddressToHubDto,storeDeliveryUser);
        deliveryRepository.save(delivery);
        StringBuilder productName=new StringBuilder();
        List<ProductRequestDto> productRequestDto = kafkaRequireAddressToHubDto.getProducts();
        for(int i=0; i<productRequestDto.size(); i++){

            productName.append(productRequestDto.get(i).getProductName());
            if(i!=productRequestDto.size()-1){
                productName.append(", ");
            }
        }
        // depart와 arrive가 같은 경우는 처리하지 않음
        for(int i=0; i<hubRouteDto.getVisitHubName().size()-1; i++){
            // 여기에 시퀀스 넣어줘야함
            // 요청할 때, 0번째, 1번째 같이 보내줘야함
            String depart = hubRouteDto.getVisitHubName().get(i);
            String arrive = hubRouteDto.getVisitHubName().get(i+1);
            HubDeliveryUser hubDeliveryUser = deliveryUserAssignmentService.assignHubDeliveryUser();
            HubDirectDto realHubDirectDto = deliveryDetailService.getDirectHub(depart,arrive);
            HubDirectDto expectedHubDirectDto = hubClient.getDirectHub(depart,arrive);
            DeliveryDetail deliveryDetail = DeliveryDetail.of(hubDeliveryUser, realHubDirectDto,expectedHubDirectDto,i+1, kafkaRequireAddressToHubDto.getOrderId());
            deliveryDetailRepository.save(deliveryDetail);
        }
        slackMessageProducer.slackMessageEvent(slackMessageInfoDTO.of(hubRouteDto, storeDeliveryUser, kafkaRequireAddressToHubDto,productName.toString()));
    }
}
