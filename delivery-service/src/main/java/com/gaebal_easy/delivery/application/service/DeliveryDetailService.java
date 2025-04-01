package com.gaebal_easy.delivery.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.delivery.application.dto.DeliveryDetailDto;
import com.gaebal_easy.delivery.application.dto.NaverRouteResponse;
import com.gaebal_easy.delivery.presentation.feign.HubClient;
import com.gaebal_easy.delivery.presentation.feign.HubDirectDto;
import com.gaebal_easy.delivery.presentation.feign.HubLocationDto;
import com.gaebal_easy.delivery.domain.entity.DeliveryDetail;
import com.gaebal_easy.delivery.domain.enums.DeliveryStatus;
import com.gaebal_easy.delivery.domain.repository.DeliveryDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryDetailService {

    private final DeliveryDetailRepository deliveryDetailRepository;
    private final NaverDirectionApiService naverDirectionApiService;
    private final HubClient hubClient;

    public DeliveryDetailDto getDetailDelivery(UUID id) {
        DeliveryDetail deliveryDetail = deliveryDetailRepository.getDetailDelivery(id).orElseThrow(() -> new NullPointerException("존재하지 않는 주문입니다."));
        return DeliveryDetailDto.of(deliveryDetail);
    }

    public void deleteDetailDelivery(UUID id) {
        DeliveryDetail deliveryDetail = deliveryDetailRepository.getDetailDelivery(id).orElseThrow(() -> new NullPointerException("존재하지 않는 주문입니다."));
        deliveryDetail.delete("user name");
        deliveryDetailRepository.save(deliveryDetail);
    }


    public DeliveryDetailDto updateDetailDelivery(UUID id, DeliveryStatus status) {
        DeliveryDetail deliveryDetail = deliveryDetailRepository.getDetailDelivery(id).orElseThrow(() -> new NullPointerException("존재하지 않는 주문입니다."));
        deliveryDetail.updateStatus(status);
        deliveryDetailRepository.save(deliveryDetail);
        return DeliveryDetailDto.of(deliveryDetail);
    }

    public HubDirectDto getDirectHub(String depart, String arrive) {
        depart = depart.replaceAll("\\s+", "");
        arrive = arrive.replaceAll("\\s+", "");

        int duration=0;
        double distance=0;
        HubLocationDto departLocation = hubClient.getCoordinate(depart);
        HubLocationDto arriveLocation = hubClient.getCoordinate(arrive);
        String json = naverDirectionApiService.getDirection(departLocation.getLongitude(), departLocation.getLatitude(),
                arriveLocation.getLongitude(), arriveLocation.getLatitude());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NaverRouteResponse response = objectMapper.readValue(json, NaverRouteResponse.class);
            distance = response.getRoute().getTrafast().get(0).getSummary().getDistance();
            duration = response.getRoute().getTrafast().get(0).getSummary().getDuration();
        }
        catch (JsonProcessingException e) {
            System.out.println("JSON 파싱 오류: " + e.getMessage());
        }

        return new HubDirectDto(depart,arrive, duration, distance);
    }
}
