package com.gaebal_easy.delivery.application.service;

import com.gaebal_easy.delivery.application.dto.DeliveryDto;
import com.gaebal_easy.delivery.domain.entity.Delivery;
import com.gaebal_easy.delivery.domain.enums.DeliveryStatus;
import com.gaebal_easy.delivery.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {
	private final DeliveryRepository deliveryRepository;
	//배송담당자 지정 로직


	public DeliveryDto getDelivery(UUID id) {
		Delivery delivery = deliveryRepository.getDelivery(id).orElseThrow(()->new NullPointerException("존재하지 않는 주문입니다."));
		return DeliveryDto.of(delivery);
	}

	public void deleteDelivery(UUID id) {
		Delivery delivery = deliveryRepository.getDelivery(id).orElseThrow(()->new NullPointerException("존재하지 않는 주문입니다."));
		delivery.delete("user name");
		deliveryRepository.save(delivery);
	}


	public DeliveryDto updateDelivery(UUID id, DeliveryStatus status) {
		Delivery delivery = deliveryRepository.getDelivery(id).orElseThrow(()->new NullPointerException("존재하지 않는 주문입니다."));
		delivery.updateStatus(status);
		deliveryRepository.save(delivery);
		return DeliveryDto.of(delivery);
	}
}
