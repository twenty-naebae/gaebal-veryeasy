package com.gaebal_easy.delivery.application.service;

import com.gaebal_easy.delivery.application.dto.DeliveryDetailDto;
import com.gaebal_easy.delivery.domain.entity.DeliveryDetail;
import com.gaebal_easy.delivery.domain.enums.DeliveryStatus;
import com.gaebal_easy.delivery.domain.repository.DeliveryDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryDetailService {

    private final DeliveryDetailRepository deliveryDetailRepository;
    public DeliveryDetailDto getDetailDelivery(UUID id) {
        DeliveryDetail deliveryDetail = deliveryDetailRepository.getDetailDelivery(id).orElseThrow(()->new NullPointerException("존재하지 않는 주문입니다."));
        return DeliveryDetailDto.of(deliveryDetail);
    }

    public void deleteDetailDelivery(UUID id) {
        DeliveryDetail deliveryDetail = deliveryDetailRepository.getDetailDelivery(id).orElseThrow(()->new NullPointerException("존재하지 않는 주문입니다."));
        deliveryDetail.delete("user name");
        deliveryDetailRepository.save(deliveryDetail);
    }


    public DeliveryDetailDto updateDetailDelivery(UUID id, DeliveryStatus status) {
        DeliveryDetail deliveryDetail = deliveryDetailRepository.getDetailDelivery(id).orElseThrow(()->new NullPointerException("존재하지 않는 주문입니다."));
        deliveryDetail.updateStatus(status);
        deliveryDetailRepository.save(deliveryDetail);
        return DeliveryDetailDto.of(deliveryDetail);
    }
}
