package com.gaebal_easy.client.store.application.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KafkaStoreCreateDto {
	private UUID id;
	private String address;
}
