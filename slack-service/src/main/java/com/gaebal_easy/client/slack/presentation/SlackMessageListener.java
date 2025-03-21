package com.gaebal_easy.client.slack.presentation;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.gaebal_easy.client.slack.application.service.GeminiService;
import com.gaebal_easy.client.slack.presentation.dto.SlackRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlackMessageListener {

	private final GeminiService geminiService;

	@KafkaListener(topics = "delivery-assigned-topic", groupId = "slack-message-group")
	public void listen(String event) {
		// event는 "slackUserId:message" 형식으로 받음
		String[] eventParts = event.split(":");
		if (eventParts.length == 2) {
			String slackUserId = eventParts[0];
			UUID receiveId = UUID.fromString(eventParts[1]);


		}
	}
}
