package com.gaebal_easy.client.store.application.dto;

import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class StoreResponse {
	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder
	public static class postStoreResponse {
		private UUID id;
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder
	public static class getStoreListResponse {
		private List<StoreDTO> stores;
		private int totalPages;
		private int currentPage;
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder
	public static class getSearchedStoreListResponse {
		private List<SearchedStoreDTO> stores;
		private int totalPages;
		private int currentPage;
	}
}
