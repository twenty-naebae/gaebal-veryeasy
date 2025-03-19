package com.gaebal_easy.order.application.service;

import com.gaebal_easy.order.application.dto.OrderResponse;
import com.gaebal_easy.order.domain.entity.Order;
import com.gaebal_easy.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponse getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return OrderResponse.from(order);
    }
}
