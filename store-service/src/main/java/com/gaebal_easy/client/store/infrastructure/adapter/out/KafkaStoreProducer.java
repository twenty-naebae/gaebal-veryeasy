package com.gaebal_easy.client.store.infrastructure.adapter.out;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.store.application.dto.KafkaStoreCreateDto;
import com.gaebal_easy.client.store.application.dto.StoreInfoKafkaDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaStoreProducer {

	private static final String STORE_TOPIC = "store-order-info";
	private static final String STORE_CREATE_TOPIC = "store_create";
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public void sendStoreInfo(StoreInfoKafkaDTO storeInfoKafkaDTO) {
		try {
			String event = objectMapper.writeValueAsString(storeInfoKafkaDTO);

			kafkaTemplate.send(STORE_TOPIC, event);
			log.info("Sent message to Kafka: {}", event);
		} catch (JsonProcessingException e) {
			log.error("Error serializing StoreInfoDTO", e);
		}
	}

	public void sendStoreCreate(KafkaStoreCreateDto kafkaStoreCreateDto) {
		try {
			String event = objectMapper.writeValueAsString(kafkaStoreCreateDto);

			kafkaTemplate.send(STORE_CREATE_TOPIC, event);
			log.info("Sent message to Kafka: {}", event);
		} catch (JsonProcessingException e) {
			log.error("Error serializing StoreInfoDTO", e);
		}
	}
}
