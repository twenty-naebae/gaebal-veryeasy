package com.gaebal_easy.delivery.infrastructure.adapter.in;

import com.gaebal_easy.delivery.presentation.dto.kafkaConsumerDto.KafkaRequireAddressToHubDto;
import com.gaebal_easy.delivery.presentation.dto.kafkaConsumerDto.ProductRequestDto;
import com.gaebal_easy.delivery.presentation.dto.kafkaProducerDto.SlackMessageInfoDTO;
import com.gaebal_easy.delivery.presentation.feign.HubClient;
import com.gaebal_easy.delivery.presentation.feign.HubDirectDto;
import com.gaebal_easy.delivery.presentation.feign.HubRouteDto;
import com.gaebal_easy.delivery.application.service.DeliveryDetailService;
import com.gaebal_easy.delivery.application.service.DeliveryUserAssignmentService;
import com.gaebal_easy.delivery.infrastructure.adapter.out.SlackMessageProducer;
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
import java.util.UUID;

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
        HubRouteDto hubRouteDto = getHubRouteDto(kafkaRequireAddressToHubDto);
        StoreDeliveryUser storeDeliveryUser = deliveryUserAssignmentService.assignStoreDeliveryUser(kafkaRequireAddressToHubDto.getArriveHubId());
        deliveryRepository.save(Delivery.of(hubRouteDto,kafkaRequireAddressToHubDto,storeDeliveryUser));
        String productName = getProductName(kafkaRequireAddressToHubDto);
        saveDeliveryDetail(hubRouteDto,kafkaRequireAddressToHubDto.getOrderId());

        slackMessageProducer.slackMessageEvent(SlackMessageInfoDTO.of(hubRouteDto, storeDeliveryUser, kafkaRequireAddressToHubDto,productName));
    }

    private String getProductName(KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto){
        StringBuilder productName=new StringBuilder();
        List<ProductRequestDto> productRequestDto = kafkaRequireAddressToHubDto.getProducts();
        for(int i=0; i<productRequestDto.size(); i++){

            productName.append(productRequestDto.get(i).getProductName());
            if(i!=productRequestDto.size()-1){
                productName.append(", ");
            }
        }
        return productName.toString();
    }

    private HubRouteDto getHubRouteDto(KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto){
        return hubClient.getHubRoute(kafkaRequireAddressToHubDto.getSupplyStoreHubName().substring(0,4),
                kafkaRequireAddressToHubDto.getReceiptStoreHubName().substring(0,4));
    }

    private void saveDeliveryDetail(HubRouteDto hubRouteDto, UUID orderId){
        for(int i=0; i<hubRouteDto.getVisitHubName().size()-1; i++){
            String depart = hubRouteDto.getVisitHubName().get(i);
            String arrive = hubRouteDto.getVisitHubName().get(i+1);
            HubDeliveryUser hubDeliveryUser = deliveryUserAssignmentService.assignHubDeliveryUser();
            HubDirectDto realHubDirectDto = deliveryDetailService.getDirectHub(depart,arrive);
            HubDirectDto expectedHubDirectDto = hubClient.getDirectHub(depart,arrive);
            DeliveryDetail deliveryDetail = DeliveryDetail.of(hubDeliveryUser, realHubDirectDto,expectedHubDirectDto,i+1, orderId);
            deliveryDetailRepository.save(deliveryDetail);
        }
    }
}
