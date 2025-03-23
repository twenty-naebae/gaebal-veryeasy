package com.gaebal_easy.order.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.order.application.dto.CreateOrderDto;
import com.gaebal_easy.order.application.dto.OrderResponse;
import com.gaebal_easy.order.application.dto.UpdateOrderDto;
import com.gaebal_easy.order.domain.entity.Order;
import com.gaebal_easy.order.domain.entity.OrderProduct;
import com.gaebal_easy.order.domain.repository.OrderRepository;
import com.gaebal_easy.order.presentation.dto.ProductDto;
import com.gaebal_easy.order.presentation.dto.StockCheckRequest;
import gaebal_easy.common.global.dto.ApiResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
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

    public String createOrder(CreateOrderDto dto) {
        Boolean availableStock = checkStock(dto);

        Long totalPrice = calculateTotalPrice(dto);

        if(availableStock){
            Order order = Order.create(dto.getSupplier(), dto.getReceiver(), dto.getOrderRequest(), dto.getAddress(), totalPrice);

            for(ProductDto product: dto.getProducts()){
                OrderProduct orderProduct = OrderProduct.create(product.getProductId(), product.getQuantity());
                order.addOrderProduct(orderProduct);

            }
            orderRepository.save(order);
        }

        return "test";
    }

    private static Long calculateTotalPrice(CreateOrderDto dto) {
        Long totalPrice = 0L;
        for(ProductDto product : dto.getProducts()){
            totalPrice+=(product.getPrice() * product.getQuantity());
        }
        return totalPrice;
    }


    public Boolean checkStock(CreateOrderDto dto) {
        StockCheckRequest stockCheck = StockCheckRequest.builder()
                .products(dto.getProducts())
                .hubId(dto.getHubId())
                .build();

        ResponseEntity<?> responseEntity = hubClient.checkStock(stockCheck);
        log.info("Check stock: {}", responseEntity.getBody());

        return (Boolean) responseEntity.getBody();
    }
}
