package com.gaebal_easy.client.hub.presentation.dto.kafkaConsumerDto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreInfoKafkaDTO {
	private UUID orderId;
	private UUID receiverId;
	private UUID supplierId;
	private List<ProductRequestDto> products;
	private UUID receiverHubId;
	private UUID supplierHubId;
	private String receiverName;
	private String receiverAddress;
	private String supplierName;
}
