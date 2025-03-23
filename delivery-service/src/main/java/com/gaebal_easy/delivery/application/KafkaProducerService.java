package com.gaebal_easy.delivery.application;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.delivery.application.dto.SlackMessageInfoDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerService {

	private static final String DELIVERY_ASSIGNED_COMPLETED_TOPIC = "delivery-assigned-topic";

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public void sendDeliveryAssignedEvent(SlackMessageInfoDTO slackMessageInfoDTO) {
		try {
			// SlackMessageInfoDTO 객체를 JSON 문자열로 직렬화
			String event = objectMapper.writeValueAsString(slackMessageInfoDTO);

			// Kafka에 이벤트 발행
			kafkaTemplate.send(DELIVERY_ASSIGNED_COMPLETED_TOPIC, event);
			log.info("Sent message to Kafka: {}", event);
		} catch (JsonProcessingException e) {
			log.error("Error serializing SlackMessageInfoDTO", e);
		}
	}
}
