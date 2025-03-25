package com.gaebal_easy.client.hub.application.dto.kafkaConsumerDto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {
	private UUID productId;
	private String productName;
	private Long quantity;
}
