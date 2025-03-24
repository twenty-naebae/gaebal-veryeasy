package com.gaebal_easy.delivery.presentation;

import com.gaebal_easy.delivery.application.service.DeliveryUserService;
import com.gaebal_easy.delivery.presentation.dto.DeliveryUserInfoResponse;
import gaebal_easy.common.global.dto.ApiResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery-service/api")
public class DelvieryUserController {

    private final DeliveryUserService deliveryUserService;

    @GetMapping("/delivery-users")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<ApiResponseData<Page<DeliveryUserInfoResponse>>> getDeliveryUsers(
            @RequestParam(defaultValue = "hub") String type,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Page<DeliveryUserInfoResponse> users = deliveryUserService.getDeliveryUsers(type, sort, page, size);
        return ResponseEntity.ok(ApiResponseData.success(users, "배송 담당자 리스트 조회 성공"));
    }

}
