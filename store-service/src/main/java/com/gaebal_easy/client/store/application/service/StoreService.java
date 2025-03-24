package com.gaebal_easy.client.store.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaebal_easy.client.store.application.service.mapper.StoreMapper;
import com.gaebal_easy.client.store.domain.entity.Store;
import com.gaebal_easy.client.store.domain.repository.StoreRepository;
import com.gaebal_easy.client.store.exception.StoreException;
import com.gaebal_easy.client.store.presentation.UserServiceClient;
import com.gaebal_easy.client.store.presentation.dto.OrderCreateKafkaDto;
import com.gaebal_easy.client.store.presentation.dto.SearchedStoreDTO;
import com.gaebal_easy.client.store.presentation.dto.StoreDTO;
import com.gaebal_easy.client.store.presentation.dto.StoreInfoKafkaDTO;
import com.gaebal_easy.client.store.presentation.dto.StoreRequest;
import com.gaebal_easy.client.store.presentation.dto.StoreResponse;

import gaebal_easy.common.global.enums.Role;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {
	private final StoreRepository storeRepository;
	private final KafkaProducerService kafkaProducerService;
	private final UserServiceClient userServiceClient;

	@Transactional(readOnly = true)
	public StoreResponse.getStoreListResponse getStoreList(UUID hubId, int page, int size) {
		if (size != 10 && size != 30 && size != 50) {
			size = 10;
		}
		Sort sort = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("updatedAt"));
		Pageable pageable = PageRequest.of(page - 1, size, sort);
		Page<Store> storePage = storeRepository.findAllByHubId(hubId, pageable);

		List<StoreDTO> storeDTOS = StoreMapper.toStoreDTOList(storePage.getContent());

		return StoreResponse.getStoreListResponse.builder()
			.stores(storeDTOS)
			.currentPage(page)
			.totalPages(storePage.getTotalPages())
			.build();
	}

	public StoreResponse.postStoreResponse postStore(String access, UUID hubId, Long managerId, StoreRequest.postStoreRequest request) {
		// Role managerRole = userServiceClient.getUserRole(access, managerId);
		// if(managerRole != Role.STORE_MANAGER) throw new StoreException.StoreManagerNotFoundException();
		Store store = Store.create(hubId, managerId, request);
		Store createdStore = storeRepository.save(store);
		return StoreResponse.postStoreResponse.builder().id(createdStore.getId()).build();
	}

	public void updateStore(UUID storeId, StoreRequest.updateStoreRequest request) {
		Store store = getStore(storeId);
		store.update(request);
	}

	private Store getStore(UUID storeId) {
		return storeRepository.findById(storeId).orElseThrow(StoreException.StoreNotFoundException::new);
	}

	public void deleteStore(UUID storeId) {
		getStore(storeId);
		storeRepository.deleteById(storeId);
	}

	public StoreResponse.getSearchedStoreListResponse getSearchedStoreList(StoreRequest.searchStoreRequest request, int page, int size) {
		if (size != 10 && size != 30 && size != 50) {
			size = 10;
		}
		Sort sort = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("updatedAt"));
		Pageable pageable = PageRequest.of(page - 1, size, sort);
		Page<Store> storePage = storeRepository.findByNameContainingOrAddressContaining(request.getName(), request.getAddress(), pageable);
		List<SearchedStoreDTO> searchedStoreDTOS = StoreMapper.toSearchedStoreDTOList(storePage.getContent());
		return StoreResponse.getSearchedStoreListResponse.builder()
			.stores(searchedStoreDTOS)
			.currentPage(page)
			.totalPages(storePage.getTotalPages())
			.build();
	}

	public void getStoreInfo(OrderCreateKafkaDto orderRequest) {
		UUID receiverId = orderRequest.getReceiverId();
		UUID supplierId = orderRequest.getSupplierId();
		Store receiverStore = getStore(receiverId);
		Store supplierStore = getStore(supplierId);
		StoreInfoKafkaDTO storeInfoKafkaDTO = StoreInfoKafkaDTO.builder()
			.orderId(orderRequest.getOrderId())
			.receiverId(receiverId)
			.supplierId(supplierId)
			.products(orderRequest.getProducts())
			.receiverHubId(receiverStore.getHubId())
			.supplierHubId(receiverStore.getHubId())
			.receiverName(receiverStore.getName())
			.receiverAddress(receiverStore.getAddress())
			.supplierName(supplierStore.getName())
			.build();
		kafkaProducerService.sendStoreInfo(storeInfoKafkaDTO);
	}
}
