package com.gaebal_easy.client.slack.application.service;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gaebal_easy.client.slack.exception.SlackException;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.conversations.ConversationsOpenRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsOpenResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlackService {
	@Value("${slack.token}")
	private String slackBotToken;

	public void sendMessage(String message, String slackUserId) {
		try {
			Slack slack = Slack.getInstance();

			ConversationsOpenResponse openResponse = slack.methods(slackBotToken)
				.conversationsOpen(ConversationsOpenRequest.builder()
					.users(Collections.singletonList(slackUserId)) //리스트 형태
					.build());

			if (!openResponse.isOk()) {
				throw new SlackException.SlackDMChannelException();
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
					throw new SlackException.SlackNotAuthorizedException();
				} else if ("user_not_found".equals(errorCode) || "channel_not_found".equals(errorCode)) {
					throw new SlackException.SlackInvalidUserException();
				} else if ("rate_limited".equals(errorCode)) {
					throw new SlackException.SlackRateLimitedException();
				} else {
					throw new SlackException.SlackMessageException();
				}
			}


		} catch (IOException | SlackApiException e) {
			e.printStackTrace();
		}
	}
}
