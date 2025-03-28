package com.gaebal_easy.client.slack.infrastructure.adapter.out;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.slack.application.dto.SendSlackMessageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaSlackMessageProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;

	private static final String SLACK_TOPIC = "slack-message-topic";

	private final ObjectMapper objectMapper;

	public void sendSlackMessage(SendSlackMessageDTO sendSlackMessageDTO) {
		try {
			String event = objectMapper.writeValueAsString(sendSlackMessageDTO);

			kafkaTemplate.send(SLACK_TOPIC, event);
			log.info("Sent message to Kafka: {}", event);
		} catch (JsonProcessingException e) {
			log.error("Error serializing StoreInfoDTO", e);
		}
	}
}

