package com.gaebal_easy.client.slack.presentation.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.slack.application.service.GeminiService;
import com.gaebal_easy.client.slack.presentation.dto.SlackMessageInfoDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackMessageListener {

	private final GeminiService geminiService;
	private final ObjectMapper objectMapper;

	@KafkaListener(topics = "delivery-assigned-topic", groupId = "slack-message-group")
	public void listen(String event) {

		try {
			// Kafka에서 받은 event (JSON 문자열)을 SlackMessageInfoDTO 객체로 역직렬화
			SlackMessageInfoDTO slackMessageInfoDTO = objectMapper.readValue(event, SlackMessageInfoDTO.class);

			// 필요한 로직 처리 (예: geminiService 사용)
			geminiService.generateAndSendDeadline(slackMessageInfoDTO);
			log.info("Received message from Kafka: {}", slackMessageInfoDTO);
		} catch (JsonProcessingException e) {
			log.error("Error deserializing Kafka message", e);
		}
	}
}
