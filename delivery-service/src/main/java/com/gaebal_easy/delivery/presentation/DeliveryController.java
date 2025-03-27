package com.gaebal_easy.delivery.presentation;


import com.gaebal_easy.delivery.application.service.DeliveryDetailService;
import com.gaebal_easy.delivery.application.service.DeliveryService;
import com.gaebal_easy.delivery.domain.enums.DeliveryStatus;
import gaebal_easy.common.global.dto.ApiResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery-service/api")
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final DeliveryDetailService deliveryDetailService;

    @GetMapping("/delivery/{id}")
    public ResponseEntity<?> getDelivery(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponseData.success(deliveryService.getDelivery(id)));
    }

    @DeleteMapping("/delivery/{id}")
    public ResponseEntity<?> deleteDelivery(@PathVariable UUID id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.ok(ApiResponseData.success(null));
    }

    @PatchMapping("/delivery/{id}")
    public ResponseEntity<?> updateDelivery(@PathVariable UUID id, @RequestParam DeliveryStatus status) {
        return ResponseEntity.ok(ApiResponseData.success(deliveryService.updateDelivery(id, status)));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetailDelivery(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponseData.success(deliveryDetailService.getDetailDelivery(id)));
    }

    @DeleteMapping("/detail/{id}")
    public ResponseEntity<?> deleteDetailDelivery(@PathVariable UUID id) {
        deliveryDetailService.deleteDetailDelivery(id);
        return ResponseEntity.ok(ApiResponseData.success(null));
    }

    @PatchMapping("/detail/{id}")
    public ResponseEntity<?> updateDetailDelivery(@PathVariable UUID id, @RequestParam DeliveryStatus status) {
        return ResponseEntity.ok(ApiResponseData.success(deliveryDetailService.updateDetailDelivery(id, status)));
    }
}
