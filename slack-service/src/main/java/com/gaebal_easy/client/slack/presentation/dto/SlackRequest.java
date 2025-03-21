package com.gaebal_easy.client.slack.presentation.dto;

import java.util.UUID;

import lombok.Getter;

public class SlackRequest {

	@Getter
	public static class GenerateDeadLineRequest {
		private UUID orderNumber;           // 주문 번호
		private String customerName;       // 주문자 이름
		private String productInfo;        // 상품 정보
		private String requestDetails;     // 요청 사항
		private String shippingAddress;    // 발송지
		private String transitAddress;     // 경유지
		private String destination;        // 도착지
		private String deliveryManager;    // 배송 담당자
	}

}