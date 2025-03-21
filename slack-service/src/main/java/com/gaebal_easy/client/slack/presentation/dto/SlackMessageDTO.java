package com.gaebal_easy.client.slack.presentation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SlackMessageDTO {
	private UUID slackMessageId;
	private String message;
	private LocalDateTime sendTime;
}
