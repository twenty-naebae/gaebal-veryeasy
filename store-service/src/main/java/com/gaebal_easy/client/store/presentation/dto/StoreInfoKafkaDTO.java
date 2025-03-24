package com.gaebal_easy.client.store.presentation.dto;

import java.util.List;
import java.util.UUID;

import lombok.Builder;

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
