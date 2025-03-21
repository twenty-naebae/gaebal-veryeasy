package com.gaebal_easy.delivery.application.dto;

import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SlackMessageInfoDTO {
	private String slackId; //배송담당자 슬랙 아이디
	private UUID receiveId; //슬랙을 받을 배송담당자 아이디
	private UUID orderId;           // 주문 아이디
	private String customerName;       // 주문자 이름
	private String productName;        // 상품명
	private String orderRequest;     // 요청 사항
	private String departHub;    // 출발 허브
	private List<String> visitHub;     // 경유 허브
	private String destination;        // 도착배송지주소
	private String deliveryManagerName;    // 배송 담당자 이름

}
