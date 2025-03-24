package com.gaebal_easy.client.store.presentation.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.store.application.service.StoreService;
import com.gaebal_easy.client.store.presentation.dto.OrderCreateKafkaDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreListener {

	private final ObjectMapper objectMapper;
	private final StoreService storeService;

	@KafkaListener(topics = "order_create", groupId = "store-group")
	public void listen(String event) {

		try {
			OrderCreateKafkaDto orderCreateKafkaDto = objectMapper.readValue(event, OrderCreateKafkaDto.class);

			storeService.getStoreInfo(orderCreateKafkaDto);
			log.info("Received message from Kafka: {}", orderCreateKafkaDto);
		} catch (JsonProcessingException e) {
			log.error("Error deserializing Kafka message", e);
		}
	}
}
