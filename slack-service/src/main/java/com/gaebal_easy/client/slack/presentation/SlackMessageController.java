package com.gaebal_easy.client.slack.presentation;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gaebal_easy.client.slack.application.service.GeminiService;
import com.gaebal_easy.client.slack.application.service.SlackMessageService;
import com.gaebal_easy.client.slack.application.dto.SlackMessageInfoDTO;
import com.gaebal_easy.client.slack.application.dto.SlackResponse;

import gaebal_easy.common.global.dto.ApiResponseData;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slack-service/api")
public class SlackMessageController {

	private final SlackMessageService slackMessageService;
	private final GeminiService geminiService;

	@GetMapping("/getSlack")
	@PreAuthorize("hasRole('MASTER')")
	public ResponseEntity<ApiResponseData<SlackResponse.GetSlackMessagesResponse>> getSlackMessages (
		@RequestParam UUID receiveId,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(ApiResponseData.success(slackMessageService.getMessages(receiveId, page, size)));
	}

	@PostMapping("/send")
	public String sendSlackMessages (
		@RequestBody SlackMessageInfoDTO slackMessageInfo
	) {
		geminiService.generateAndSendDeadline(slackMessageInfo);
		return "OK";
	}

}
