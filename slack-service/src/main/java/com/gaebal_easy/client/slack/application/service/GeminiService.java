package com.gaebal_easy.client.slack.application.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.slack.presentation.dto.SlackRequest;
import com.gaebal_easy.client.slack.exception.GeminiException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeminiService {

	private final WebClient aiWebClient;
	private final SlackMessageService slackMessageService;

	@Value("${ai.api.key}")
	private String apiKey;

	public void generateAndSendDeadline(SlackRequest.GenerateDeadLineRequest requestDto, String slackUserId, UUID receiveId) {
		String orderMessage = buildGeminiOrder(requestDto);

		// AI API에 보낼 payload 구성 (요청 JSON 구조)
		Map<String, Object> payload = new HashMap<>();
		Map<String, Object> contentMap = new HashMap<>();
		Map<String, String> part = new HashMap<>();
		part.put("text", orderMessage);  // 주문 정보와 요청 사항 모두 포함
		contentMap.put("parts", new Map[] {part});
		payload.put("contents", new Map[] {contentMap});

		// Gemini API 호출하여 발송 시한 생성
		String responseBody = aiWebClient.post()
			.uri(uriBuilder -> uriBuilder
				.path("/v1beta/models/gemini-1.5-flash-latest:generateContent")
				.queryParam("key", apiKey)
				.build())
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.bodyValue(payload)
			.retrieve()
			.bodyToMono(String.class)
			.block();

		// 응답에서 도출된 발송 시한 추출
		String deadline = extractDeadline(responseBody);

		// Slack 메시지 형식으로 변환
		String message = buildSlackMessage(requestDto, deadline);

		// Slack으로 메시지 전송
		slackMessageService.saveMesage(receiveId, message);
		slackMessageService.sendMessage(message, slackUserId);
	}

	@NotNull
	private static String buildSlackMessage(SlackRequest.GenerateDeadLineRequest requestDto, String deadline) {
		String message = String.format("주문 번호: %s\n" +
				"주문자 정보: %s \n" +
				"상품 정보: %s\n" +
				"요청 사항: %s\n" +
				"발송지: %s\n" +
				"경유지: %s\n" +
				"도착지: %s\n" +
				"배송담당자: %s \n\n" +
				"위 내용을 기반으로 도출된 최종 발송 시한은 %s 입니다.",
			requestDto.getOrderNumber(),
			requestDto.getCustomerName(),
			requestDto.getProductInfo(),
			requestDto.getRequestDetails(),
			requestDto.getShippingAddress(),
			requestDto.getTransitAddress(),
			requestDto.getDestination(),
			requestDto.getDeliveryManager(),
			deadline);
		return message;
	}

	private String buildGeminiOrder(SlackRequest.GenerateDeadLineRequest requestDto) {
		return String.format(
			"주문 번호: %s\n" +
				"주문자 정보: %s\n" +
				"상품 정보: %s\n" +
				"요청 사항: %s\n" +
				"발송지: %s\n" +
				"경유지: %s\n" +
				"도착지: %s\n" +
				"배송담당자: %s\n\n" +
				"위 내용 기반으로 도출된 발송 시한을 날짜, 시간까지 생성해주세요.",
			requestDto.getOrderNumber(),
			requestDto.getCustomerName(),
			requestDto.getProductInfo(),
			requestDto.getRequestDetails(),
			requestDto.getShippingAddress(),
			requestDto.getTransitAddress(),
			requestDto.getDestination(),
			requestDto.getDeliveryManager()
		);
	}

	private String extractDeadline(String responseBody) {
		// 응답 JSON 구조에서 발송 시한 추출
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode root = objectMapper.readTree(responseBody);
			JsonNode candidates = root.path("candidates");
			if (candidates.isArray() && !candidates.isEmpty()) {
				JsonNode candidate = candidates.get(0);
				JsonNode content = candidate.path("content");
				JsonNode parts = content.path("parts");
				if (parts.isArray() && !parts.isEmpty()) {
					return parts.get(0).path("text").asText();
				}
			}
		} catch (Exception e) {
			throw new GeminiException.GeminiParsingException();
		}
		return "";
	}
}
