package com.gaebal_easy.order.presentation;

import com.gaebal_easy.order.application.dto.OrderResponse;
import com.gaebal_easy.order.application.service.OrderService;
import com.gaebal_easy.order.domain.entity.Order;
import com.gaebal_easy.order.presentation.dto.OrderResponseDto;
import gaebal_easy.common.global.dto.ApiResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service/api")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders/{id}")
    public ResponseEntity<ApiResponseData<OrderResponseDto>> getOrder(@PathVariable UUID orderId) {
        OrderResponse orderResponse = orderService.getOrder(orderId);
        return ResponseEntity.ok(ApiResponseData.success(OrderResponseDto.from(orderResponse)));

    }


}
