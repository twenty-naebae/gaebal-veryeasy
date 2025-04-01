package com.gaebal_easy.delivery.presentation.dto.kafkaProducerDto;

import com.gaebal_easy.delivery.presentation.dto.kafkaConsumerDto.KafkaRequireAddressToHubDto;
import com.gaebal_easy.delivery.presentation.feign.HubRouteDto;
import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;
import lombok.*;

import java.util.List;
import java.util.UUID;

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

	public static SlackMessageInfoDTO of(HubRouteDto hubRouteDto, StoreDeliveryUser storeDeliveryUser, KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto, String productName){
		return SlackMessageInfoDTO.builder()
				.slackId(storeDeliveryUser.getSlackId())
				.receiveId(storeDeliveryUser.getId())
				.orderId(kafkaRequireAddressToHubDto.getOrderId())
				.departHub(hubRouteDto.getDepart())
				.productName(productName)
				.customerName(null)
				.orderRequest(null)
				.visitHub(hubRouteDto.getVisitHubName())
				.destination(kafkaRequireAddressToHubDto.getReceiptStoreAddress())
				.deliveryManagerName(storeDeliveryUser.getName())
				.build();
	}
}
