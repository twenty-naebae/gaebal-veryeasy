package com.gaebal_easy.client.slack.application.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gaebal_easy.client.slack.domain.entity.SlackMessage;
import com.gaebal_easy.client.slack.domain.repository.SlackMessageRepository;
import com.gaebal_easy.client.slack.exception.SlackMessageException;
import com.gaebal_easy.client.slack.presentation.dto.SlackMessageDTO;
import com.gaebal_easy.client.slack.presentation.dto.SlackResponse;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.conversations.ConversationsOpenRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsOpenResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlackMessageService {
	@Value("${slack.token}")
	private String slackBotToken;

	private final SlackMessageRepository slackMessageRepository;

	public void sendMessage(String message, String slackUserId) {
		try {
			Slack slack = Slack.getInstance();

			ConversationsOpenResponse openResponse = slack.methods(slackBotToken)
				.conversationsOpen(ConversationsOpenRequest.builder()
					.users(Collections.singletonList(slackUserId)) //리스트 형태
					.build());

			if (!openResponse.isOk()) {
				throw new SlackMessageException.SlackDMChannelMessageException();
			}

			String dmChannelId = openResponse.getChannel().getId();

			// 해당 DM 채널로 메시지 보내기
			ChatPostMessageResponse messageResponse = slack.methods(slackBotToken)
				.chatPostMessage(ChatPostMessageRequest.builder()
					.channel(dmChannelId) // DM 채널 ID
					.text(message)
					.build());

			if (!messageResponse.isOk()) {
				String errorCode = messageResponse.getError();

				if ("invalid_auth".equals(errorCode)) {
					throw new SlackMessageException.SlackNotAuthorizedMessageException();
				} else if ("user_not_found".equals(errorCode) || "channel_not_found".equals(errorCode)) {
					throw new SlackMessageException.SlackInvalidUserMessageException();
				} else if ("rate_limited".equals(errorCode)) {
					throw new SlackMessageException.SlackRateLimitedMessageException();
				} else {
					throw new SlackMessageException.SlackMessageNotSendException();
				}
			}


		} catch (IOException | SlackApiException e) {
			e.printStackTrace();
		}
	}
	public void saveMesage(UUID receiveId, String message) {
		SlackMessage slackMessage = new SlackMessage(receiveId, message, LocalDateTime.now());
		slackMessageRepository.save(slackMessage);

	}

	public SlackResponse.GetSlackMessagesResponse getMessages(UUID receiveId, int page, int size) {
		if (size != 10 && size != 30 && size != 50) {
			size = 10;
		}
		Sort sort = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("updatedAt"));
		Pageable pageable = PageRequest.of(page - 1, size, sort);
		Page<SlackMessage> slackMessagePage= slackMessageRepository.findAllByReceiveId(receiveId, pageable);
		List<SlackMessageDTO> slackMessageDTOS = new ArrayList<>();
		for (SlackMessage slackMessage : slackMessagePage.getContent()) {
			slackMessageDTOS.add(SlackMessageDTO.builder()
					.slackMessageId(slackMessage.getId())
					.message(slackMessage.getMessage())
					.sendTime(slackMessage.getSendTime())
					.build());
		}
		return SlackResponse.GetSlackMessagesResponse.builder()
			.slackMessages(slackMessageDTOS)
			.totalPages(slackMessagePage.getTotalPages())
			.currentPage(page)
			.build();
	}
}
