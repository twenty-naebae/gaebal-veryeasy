package com.gaebal_easy.client.slack.application.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class GeminiResponse {

	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder
	public static class GenerateDeadlineResponse {
		private String deadline;
	}

}