package com.gaebal_easy.client.store.presentation.mapper;

import java.util.ArrayList;
import java.util.List;

import com.gaebal_easy.client.store.domain.entity.Store;
import com.gaebal_easy.client.store.presentation.dto.SearchedStoreDTO;
import com.gaebal_easy.client.store.presentation.dto.StoreDTO;

public class StoreMapper {

	public static StoreDTO toStoreDTO(Store store) {
		return StoreDTO.builder()
			.id(store.getId())
			.name(store.getName())
			.type(store.getType())
			.address(store.getAddress())
			.managerId(store.getManagerId())
			.build();
	}

	public static SearchedStoreDTO toSearchedStoreDTO(Store store) {
		return SearchedStoreDTO.builder()
			.id(store.getId())
			.name(store.getName())
			.hubId(store.getHubId())
			.type(store.getType())
			.address(store.getAddress())
			.managerId(store.getManagerId())
			.build();
	}

	public static List<StoreDTO> toStoreDTOList(List<Store> stores) {
		List<StoreDTO> storeDTOS = new ArrayList<>();
		for (Store store : stores) {
			storeDTOS.add(toStoreDTO(store));
		}
		return storeDTOS;
	}

	public static List<SearchedStoreDTO> toSearchedStoreDTOList(List<Store> stores) {
		List<SearchedStoreDTO> storeDTOS = new ArrayList<>();
		for (Store store : stores) {
			storeDTOS.add(toSearchedStoreDTO(store));
		}
		return storeDTOS;
	}


}
