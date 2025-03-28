package com.gaebal_easy.client.slack.infrastructure.adapter.in;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.slack.application.service.GeminiService;
import com.gaebal_easy.client.slack.application.service.SlackMessageService;
import com.gaebal_easy.client.slack.application.dto.SendSlackMessageDTO;
import com.gaebal_easy.client.slack.application.dto.SlackMessageInfoDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaSlackMessageConsumer {

	private final GeminiService geminiService;
	private final ObjectMapper objectMapper;
	private final SlackMessageService slackMessageService;

	@KafkaListener(topics = "delivery-assigned-topic", groupId = "slack-message-group")
	public void listen(String event) {

		try {
			SlackMessageInfoDTO slackMessageInfoDTO = objectMapper.readValue(event, SlackMessageInfoDTO.class);

			geminiService.generateAndSendDeadline(slackMessageInfoDTO);
			log.info("Received message from Kafka: {}", slackMessageInfoDTO);
		} catch (JsonProcessingException e) {
			log.error("Error deserializing Kafka message", e);
		}
	}

	@KafkaListener(topics = "slack-message-topic", groupId = "slack_group", concurrency = "3")
	public void consume(String event) {
		try {
			SendSlackMessageDTO sendSlackMessageDTO = objectMapper.readValue(event, SendSlackMessageDTO.class);

			slackMessageService.sendMessage(sendSlackMessageDTO.getMessage(), sendSlackMessageDTO.getSlackUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
