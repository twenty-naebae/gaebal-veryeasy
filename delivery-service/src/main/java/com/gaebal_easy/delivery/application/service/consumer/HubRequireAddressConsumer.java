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
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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

        log.info("hubROuteDto!!!!!!!!!!"+kafkaRequireAddressToHubDto.getSupplyStoreHubName().substring(0,4) +" "+kafkaRequireAddressToHubDto.getReceiptStoreHubName().substring(0,4));
        HubRouteDto hubRouteDto = hubClient.getHubRoute(kafkaRequireAddressToHubDto.getSupplyStoreHubName().substring(0,4),
                kafkaRequireAddressToHubDto.getReceiptStoreHubName().substring(0,4));
        log.info("최종 시간 " +hubRouteDto.getTotalRequiredTime());
        log.info("📦 HubRouteDto 정보: {}", hubRouteDto);

        log.info("3번째 시도");

        log.info("경로 가져오기 성공!!!!!!!!!!!!!!!!!!!!");
        StoreDeliveryUser storeDeliveryUser = deliveryUserAssignmentService.assignStoreDeliveryUser(kafkaRequireAddressToHubDto.getArriveHubId());
        log.info("스토어 딜리버리 유저 가져오기 성공!!!!!!!!!!!!!!!!!!!!");
        // delivery에 위에서 받아온 내용 저장!
        Delivery delivery = Delivery.of(hubRouteDto,kafkaRequireAddressToHubDto,storeDeliveryUser);
        deliveryRepository.save(delivery);
        log.info("배달에 저장 성공!!!!!!!!!!!!!!!!!!!!");

        StringBuilder productName=new StringBuilder();
        List<ProductRequestDto> productRequestDto = kafkaRequireAddressToHubDto.getProducts();
        for(int i=0; i<productRequestDto.size(); i++){

            productName.append(productRequestDto.get(i).getProductName());
            if(i!=productRequestDto.size()-1){
                productName.append(", ");
            }
        }
        log.info("상품 만들기성공!!!!!!!!!!!!!!!!!!!!");
        log.info("방문해야 하는 허브 갯수");
        log.info(hubRouteDto.getVisitHubName().size()+"방문해야 하는 허브 갯수");
        // depart와 arrive가 같은 경우는 처리하지 않음
        for(int i=0; i<hubRouteDto.getVisitHubName().size()-1; i++){
            // 여기에 시퀀스 넣어줘야함
            // 요청할 때, 0번째, 1번째 같이 보내줘야함
            log.info("hello!!!!!!!!!!!!!!!!!!!!!!!!");
            String depart = hubRouteDto.getVisitHubName().get(i);
            String arrive = hubRouteDto.getVisitHubName().get(i+1);
            log.info("depart!!!!!!"+depart+"arrive!!!!!"+arrive);
            HubDeliveryUser hubDeliveryUser = deliveryUserAssignmentService.assignHubDeliveryUser();
            log.info("허브 딜리버리 유저 성공!!!!!!!!!!!!!!!!!!!!!!");
            HubDirectDto realHubDirectDto = deliveryDetailService.getDirectHub(depart,arrive);
            log.info("실제 경로 로직 성공!!!!!!!!!!!!!!!!!!!!!!!!");
            HubDirectDto expectedHubDirectDto = hubClient.getDirectHub(depart,arrive);
            DeliveryDetail deliveryDetail = DeliveryDetail.of(hubDeliveryUser, realHubDirectDto,expectedHubDirectDto,i+1, kafkaRequireAddressToHubDto.getOrderId());
            deliveryDetailRepository.save(deliveryDetail);
        }
        log.info("\uD83C\uDFAF 목표 도달! \uD83C\uDFAF 목표 도달! \uD83C\uDFAF 목표 도달! \uD83C\uDFAF 목표 도달! \uD83C\uDFAF 목표 도달!");
        slackMessageProducer.slackMessageEvent(slackMessageInfoDTO.of(hubRouteDto, storeDeliveryUser, kafkaRequireAddressToHubDto,productName.toString()));
    }
}
