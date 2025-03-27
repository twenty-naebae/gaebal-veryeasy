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
import com.gaebal_easy.client.slack.presentation.dto.SendSlackMessageDTO;
import com.gaebal_easy.client.slack.presentation.dto.SlackMessageInfoDTO;
import com.gaebal_easy.client.slack.exception.GeminiException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GeminiService {

	private final WebClient aiWebClient;
	private final SlackMessageService slackMessageService;
	private final KafkaProducerService kafkaProducerService;

	@Value("${ai.api.key}")
	private String apiKey;

	public void generateAndSendDeadline(SlackMessageInfoDTO slackMessageInfoDTO) {
		UUID receiveId = slackMessageInfoDTO.getReceiveId();
		String slackUserId = slackMessageInfoDTO.getSlackId();
		String orderMessage = buildGeminiOrder(slackMessageInfoDTO);

		// AI API에 보낼 payload 구성 (요청 JSON 구조)
		Map<String, Object> payload = new HashMap<>();
		Map<String, Object> contentMap = new HashMap<>();
		Map<String, String> part = new HashMap<>();
		part.put("text", orderMessage);  // 주문 정보와 요청 사항 모두 포함
		contentMap.put("parts", new Map[] {part});
		payload.put("contents", new Map[] {contentMap});

		// Gemini API 호출하여 발송 시한 생성
		aiWebClient.post()
			.uri(uriBuilder -> uriBuilder
				.path("/v1beta/models/gemini-1.5-flash-latest:generateContent")
				.queryParam("key", apiKey)
				.build())
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.bodyValue(payload)
			.retrieve()
			.bodyToMono(String.class)
			.doOnTerminate(() -> {
				// 이곳에 종료 후 처리 로직을 넣을 수 있습니다. 예: 로그 남기기
			})
			.subscribe(responseBody -> {
				// 응답에서 도출된 발송 시한 추출
				String deadline = extractDeadline(responseBody);

				// Slack 메시지 형식으로 변환
				String message = buildSlackMessage(slackMessageInfoDTO, deadline);

				// Slack으로 메시지 전송
				slackMessageService.saveMesage(receiveId, message);
				SendSlackMessageDTO sendSlackMessageDTO = SendSlackMessageDTO.builder().message(message).slackUserId(slackUserId).build();
				kafkaProducerService.sendSlackMessage(sendSlackMessageDTO);
			}, error -> {
				// 에러 처리
				error.printStackTrace();
			});
	}


	@NotNull
	private static String buildSlackMessage(SlackMessageInfoDTO requestDto, String deadline) {
		String visitHubs = String.join(" ", requestDto.getVisitHub());
		String message = String.format("주문 번호: %s\n" +
				"상품 정보: %s\n" +
				"요청 사항: %s\n" +
				"발송지: %s\n" +
				"경유지: %s\n" +
				"도착지: %s\n" +
				"배송담당자: %s \n\n" +
				"위 내용을 기반으로 도출된 최종 발송 시한은 %s 입니다.",
			requestDto.getOrderId(),
			requestDto.getProductName(),
			requestDto.getOrderRequest(),
			requestDto.getDepartHub(),
			visitHubs,
			requestDto.getDestination(),
			requestDto.getDeliveryManagerName(),
			deadline);
		return message;
	}

	private String buildGeminiOrder(SlackMessageInfoDTO requestDto) {
		String visitHubs = String.join(" ", requestDto.getVisitHub());
		return String.format(
			"주문 번호: %s\n" +
				"상품 정보: %s\n" +
				"요청 사항: %s\n" +
				"발송지: %s\n" +
				"경유지: %s\n" +
				"도착지: %s\n" +
				"배송담당자: %s\n\n" +
				"위 내용 기반으로 도출된 발송 시한을 날짜, 시간까지 생성해주세요.",
			requestDto.getOrderId(),
			requestDto.getProductName(),
			requestDto.getOrderRequest(),
			requestDto.getDepartHub(),
			visitHubs,
			requestDto.getDestination(),
			requestDto.getDeliveryManagerName()
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
