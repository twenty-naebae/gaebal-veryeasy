package com.gaebal_easy.delivery.application;

import org.springframework.stereotype.Service;

import com.gaebal_easy.delivery.infrastructure.KafkaProducerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {
	private final KafkaProducerService kafkaProducerService;
	public void assignDeliveryManager(){
		log.info("assignDeliveryManager");
		kafkaProducerService.sendDeliveryAssignedEvent("U08J95TTQA0", "kafkahihihih");
	}
}
