package com.gaebal_easy.delivery.application.service;

import java.util.ArrayList;
import java.util.UUID;

import com.gaebal_easy.delivery.application.dto.DeliveryDto;
import com.gaebal_easy.delivery.domain.entity.Delivery;
import com.gaebal_easy.delivery.domain.enums.DeliveryStatus;
import com.gaebal_easy.delivery.domain.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import com.gaebal_easy.delivery.application.dto.SlackMessageInfoDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {
	private final DeliveryRepository deliveryRepository;
	private final KafkaProducerService kafkaProducerService;
	//배송담당자 지정 로직
	public void assignDeliveryManager(){
		//슬랙쪽으로 배송 정보 보내기
		//예시 dto
		SlackMessageInfoDTO slackMessageInfoDTO = SlackMessageInfoDTO.builder()
			.slackId("U08J95TTQA0")
			.receiveId(UUID.randomUUID())
			.orderId(UUID.randomUUID())
			.customerName("홍길동")
			.productName("고등어")
			.orderRequest("12월 12일 3시까지는 보내주세요!")
			.departHub("경기북부센터")
			.visitHub(new ArrayList<>())
			.destination("부산시 사하구 낙동대로 1번길 1 해산물월드")
			.deliveryManagerName("경린")
			.build();
		kafkaProducerService.sendDeliveryAssignedEvent(slackMessageInfoDTO);
	}

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
