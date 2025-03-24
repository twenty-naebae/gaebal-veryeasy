package com.gaebal_easy.client.store.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StoreType {
	PRODUCER("생산업체"),
	RECEIVER("수령업체");

	private final String value;
}
