package com.gaebal_easy.client.store.presentation.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
