package com.gaebal_easy.order.application.service;

import com.gaebal_easy.order.application.dto.CreateOrderDto;
import com.gaebal_easy.order.application.dto.OrderResponse;
import com.gaebal_easy.order.application.dto.UpdateOrderDto;
import com.gaebal_easy.order.domain.entity.Order;
import com.gaebal_easy.order.domain.repository.OrderRepository;
import com.gaebal_easy.order.presentation.dto.StockCheckRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final HubClient hubClient;

    public OrderResponse getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse updateOrder(UUID orderId, UpdateOrderDto updateOrderDto) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.changeAddress(updateOrderDto.getAddress());
        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    public String checkStock(CreateOrderDto dto) {
        StockCheckRequest stockCheck = StockCheckRequest.builder()
                .products(dto.getProducts())
                .hubId(dto.getHubId())
                .build();

        ResponseEntity<?> responseEntity = hubClient.checkStock(stockCheck);
        log.info("Check stock: {}", responseEntity.getBody());
        return "test OK";
    }
}
