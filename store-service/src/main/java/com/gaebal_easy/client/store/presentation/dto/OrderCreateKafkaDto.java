package com.gaebal_easy.client.store.presentation.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;

@Getter
public class OrderCreateKafkaDto {
	private UUID orderId;
	private UUID receiverId;
	private UUID supplierId;
	private List<ProductRequestDto> products;
}