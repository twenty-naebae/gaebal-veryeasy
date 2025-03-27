package com.gaebal_easy.order.application.service.kafka.consumer;

import com.gaebal_easy.order.domain.entity.Order;
import com.gaebal_easy.order.domain.repository.OrderRepository;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RollbackOrderKafkaConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(groupId = "err_order_response", topics = "out_of_stock" )
    public void rollbackOrderCreate(String orderId){
        UUID uuid = UUID.fromString(orderId);
        log.info("주문 보상 트랜잭션 처리 {}",orderId);
        Order order = orderRepository.findById(uuid).orElseThrow(()->new OrderNotFoundException(Code.ORDER_NOT_FOUND_EXCEIPTION));
        orderRepository.delete(order);

    }

}
