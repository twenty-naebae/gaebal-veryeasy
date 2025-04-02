package com.gaebal_easy.delivery.application.service;

import com.gaebal_easy.delivery.application.dto.DeliveryDto;
import com.gaebal_easy.delivery.application.dto.RequireAddressToHubServiceDto;
import com.gaebal_easy.delivery.domain.entity.Delivery;
import com.gaebal_easy.delivery.domain.entity.DeliveryDetail;
import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;
import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;
import com.gaebal_easy.delivery.domain.enums.DeliveryStatus;
import com.gaebal_easy.delivery.domain.repository.DeliveryDetailRepository;
import com.gaebal_easy.delivery.domain.repository.DeliveryRepository;
import com.gaebal_easy.delivery.infrastructure.adapter.out.SlackMessageProducer;
import com.gaebal_easy.delivery.infrastructure.dto.kafkaConsumerDto.KafkaRequireAddressToHubDto;
import com.gaebal_easy.delivery.infrastructure.dto.kafkaConsumerDto.ProductRequestDto;
import com.gaebal_easy.delivery.infrastructure.dto.kafkaProducerDto.SlackMessageInfoDTO;
import com.gaebal_easy.delivery.presentation.feign.HubClient;
import com.gaebal_easy.delivery.presentation.feign.HubDirectDto;
import com.gaebal_easy.delivery.presentation.feign.HubRouteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final HubClient hubClient;
    private final DeliveryDetailRepository deliveryDetailRepository;
    private final DeliveryDetailService deliveryDetailService;
    private final DeliveryUserAssignmentService deliveryUserAssignmentService;
    private final SlackMessageProducer slackMessageProducer;


    public DeliveryDto getDelivery(UUID id) {
        Delivery delivery = deliveryRepository.getDelivery(id).orElseThrow(() -> new NullPointerException("존재하지 않는 주문입니다."));
        return DeliveryDto.of(delivery);
    }

    public void deleteDelivery(UUID id) {
        Delivery delivery = deliveryRepository.getDelivery(id).orElseThrow(() -> new NullPointerException("존재하지 않는 주문입니다."));
        delivery.delete("user name");
        deliveryRepository.save(delivery);
    }


    public DeliveryDto updateDelivery(UUID id, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.getDelivery(id).orElseThrow(() -> new NullPointerException("존재하지 않는 주문입니다."));
        delivery.updateStatus(status);
        deliveryRepository.save(delivery);
        return DeliveryDto.of(delivery);
    }

    public void saveDelivery(RequireAddressToHubServiceDto requireAddressToHubServiceDto) {
        HubRouteDto hubRouteDto = getHubRouteDto(requireAddressToHubServiceDto.getSupplyStoreHubName(), requireAddressToHubServiceDto.getReceiptStoreHubName());
        StoreDeliveryUser storeDeliveryUser = deliveryUserAssignmentService.assignStoreDeliveryUser(requireAddressToHubServiceDto.getArriveHubId());
        deliveryRepository.save(Delivery.of(hubRouteDto, requireAddressToHubServiceDto, storeDeliveryUser));

        List<String> visitHubNames = hubRouteDto.getVisitHubNames();
        saveDeliveryDetail(visitHubNames, requireAddressToHubServiceDto.getOrderId());
        // TODO : outbox table에 보내야 할 메세지(SlackMessageInfoDTO)를 저장한다.
        slackMessageProducer.slackMessageEvent(
                SlackMessageInfoDTO.of(hubRouteDto,
                        storeDeliveryUser,
                        requireAddressToHubServiceDto,
                        getProductName(requireAddressToHubServiceDto.getProducts()))
        );
    }

    private String getProductName(List<ProductRequestDto> productRequestDtos) {
        return productRequestDtos.stream()
                .map(ProductRequestDto::getProductName)
                .collect(Collectors.joining(", "));
    }

    private HubRouteDto getHubRouteDto(String depart, String arrive) {
        return hubClient.getHubRoute(depart.substring(0, 4), arrive.substring(0, 4));
    }

    private void saveDeliveryDetail(List<String> visitHubNames, UUID orderId) {
        for (int i = 0; i < visitHubNames.size() - 1; i++) {
            String depart = visitHubNames.get(i);
            String arrive = visitHubNames.get(i + 1);
            HubDeliveryUser hubDeliveryUser = deliveryUserAssignmentService.assignHubDeliveryUser();
            HubDirectDto realHubDirectDto = deliveryDetailService.getDirectHub(depart, arrive);
            HubDirectDto expectedHubDirectDto = hubClient.getDirectHub(depart, arrive);
            DeliveryDetail deliveryDetail = DeliveryDetail.of(hubDeliveryUser, realHubDirectDto, expectedHubDirectDto, i + 1, orderId);
            deliveryDetailRepository.save(deliveryDetail);
        }
    }
}
