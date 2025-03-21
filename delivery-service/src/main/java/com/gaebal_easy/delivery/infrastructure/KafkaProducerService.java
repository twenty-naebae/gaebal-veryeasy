package com.gaebal_easy.delivery.infrastructure;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerService {

	private static final String DELIVERY_ASSIGNED_COMPLETED_TOPIC = "delivery-assigned-topic";

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public void sendDeliveryAssignedEvent(String slackUserId, String message) {
		String event = slackUserId + ":" + message;
		kafkaTemplate.send(DELIVERY_ASSIGNED_COMPLETED_TOPIC, event);
	}
}
