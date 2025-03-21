package com.gaebal_easy.client.slack.presentation;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gaebal_easy.client.slack.application.service.GeminiService;
import com.gaebal_easy.client.slack.application.service.SlackMessageService;
import com.gaebal_easy.client.slack.presentation.dto.SlackRequest;
import com.gaebal_easy.client.slack.presentation.dto.SlackResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slack-service/api")
public class SlackMessageController {

	private final SlackMessageService slackMessageService;
	private final GeminiService geminiService;
	//
	// @PostMapping
	// public void sendSlack () {
	// 	slackService.sendMessage("hi", "U08J95TTQA0");
	// }

	@PostMapping("/slack")
	public void sendSlack (
		@RequestBody SlackRequest.GenerateDeadLineRequest request
	) {
		geminiService.generateAndSendDeadline(request, "U08J95TTQA0", UUID.fromString("5e92fc94-f954-4e9d-95ca-76ed9cf6883d"));
	}

	@GetMapping("/getSlack")
	@PreAuthorize("hasRole('MASTER')")
	public SlackResponse.GetSlackMessagesResponse getSlackMessages (
		@RequestParam UUID receiveId,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return slackMessageService.getMessages(receiveId, page, size);
	}

}
