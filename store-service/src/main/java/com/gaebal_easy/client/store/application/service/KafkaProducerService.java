package com.gaebal_easy.client.store.application.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.store.presentation.dto.StoreInfoKafkaDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerService {

	private static final String STORE_TOPIC = "store-order-info";

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public void sendStoreInfo(StoreInfoKafkaDTO storeInfoKafkaDTO) {
		try {
			String event = objectMapper.writeValueAsString(storeInfoKafkaDTO);

			// Kafka에 이벤트 발행
			kafkaTemplate.send(STORE_TOPIC, event);
			log.info("Sent message to Kafka: {}", event);
		} catch (JsonProcessingException e) {
			log.error("Error serializing StoreInfoDTO", e);
		}
	}
}
