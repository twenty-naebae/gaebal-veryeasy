package com.gaebal_easy.client.slack.presentation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gaebal_easy.client.slack.application.service.GeminiService;
import com.gaebal_easy.client.slack.application.service.SlackService;
import com.gaebal_easy.client.slack.application.service.dto.GeminiRequest;
import com.gaebal_easy.client.slack.presentation.dtos.SlackRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slack-service/api")
public class SlackController {

	private final SlackService slackService;
	private final GeminiService geminiService;
	//
	// @PostMapping
	// public void sendSlack () {
	// 	slackService.sendMessage("hi", "U08J95TTQA0");
	// }

	@PostMapping("/slack")
	public void sendSlack (
		@RequestBody GeminiRequest.GenerateDeadLineRequest request
	) {
		geminiService.generateAndSendDeadline(request, "U08J95TTQA0");
	}

}
