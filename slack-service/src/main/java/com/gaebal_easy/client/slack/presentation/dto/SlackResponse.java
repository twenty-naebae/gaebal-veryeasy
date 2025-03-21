package com.gaebal_easy.client.slack.presentation.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class SlackResponse {

	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder
	public static class GetSlackMessagesResponse {
		private List<SlackMessageDTO> slackMessages;
		private int totalPages;
		private int currentPage;

	}
}