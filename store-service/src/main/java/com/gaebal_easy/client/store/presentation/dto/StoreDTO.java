package com.gaebal_easy.client.store.presentation.dto;

import java.util.UUID;

import com.gaebal_easy.client.store.domain.entity.StoreType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDTO {
	private UUID id;
	private String name;
	private UUID hubId;
	private StoreType type;
	private String address;
	private Long managerId;
}
