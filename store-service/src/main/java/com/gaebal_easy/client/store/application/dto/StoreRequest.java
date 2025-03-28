package com.gaebal_easy.client.store.application.dto;

import java.util.UUID;

import com.gaebal_easy.client.store.domain.entity.StoreType;

import lombok.Getter;

public class StoreRequest {
	@Getter
	public static class postStoreRequest{
		private String name;
		private StoreType type;
		private String address;
	}

	@Getter
	public static class updateStoreRequest{
		private String name;
		private StoreType type;
		private UUID hubId;
		private String address;
		private Long managerId;
	}

	@Getter
	public static class searchStoreRequest{
		private String name;
		private String address;
	}
}
