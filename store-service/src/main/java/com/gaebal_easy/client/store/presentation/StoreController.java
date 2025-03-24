package com.gaebal_easy.client.store.presentation;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gaebal_easy.client.store.application.service.StoreService;
import com.gaebal_easy.client.store.presentation.dto.StoreRequest;
import com.gaebal_easy.client.store.presentation.dto.StoreResponse;

import gaebal_easy.common.global.dto.ApiResponseData;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store-service/api")
public class StoreController {

	private final StoreService storeService;

	@PostMapping("/create")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponseData<StoreResponse.postStoreResponse>> postStore (
		@RequestParam UUID hubId,
		@RequestParam Long managerId,
		@RequestBody StoreRequest.postStoreRequest request,
		@RequestHeader("access") String access
	) {
		return ResponseEntity.ok(ApiResponseData.success(storeService.postStore(access, hubId, managerId, request)));
	}
	@PutMapping("/update")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponseData<String>> updateStore (
		@RequestParam UUID storeId,
		@RequestBody StoreRequest.updateStoreRequest request
	) {
		storeService.updateStore(storeId, request);
		return ResponseEntity.ok(ApiResponseData.success(null, "업체 수정에 성공했습니다."));
	}

	@DeleteMapping("/delete")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponseData<String>> deleteStore (
		@RequestParam UUID storeId
	) {
		storeService.deleteStore(storeId);
		return ResponseEntity.ok(ApiResponseData.success(null, "업체 삭제에 성공했습니다."));
	}

	@GetMapping("/getStores")
	public ResponseEntity<ApiResponseData<StoreResponse.getStoreListResponse>> getStoreList(
		@RequestParam UUID hubId,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(ApiResponseData.success(storeService.getStoreList(hubId, page, size)));
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponseData<StoreResponse.getSearchedStoreListResponse>> getSearchedStoreList(
		@RequestBody StoreRequest.searchStoreRequest request,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		return ResponseEntity.ok(ApiResponseData.success(storeService.getSearchedStoreList(request, page, size)));
	}

}
