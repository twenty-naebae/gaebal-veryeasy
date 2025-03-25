package com.gaebal_easy.order.application.service.kafka.consumer;

import com.gaebal_easy.order.application.dto.CreateOrderKafkaDto;
import com.gaebal_easy.order.domain.entity.Order;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckStockResponseConsumer {
    private final KafkaTemplate kafkaTemplate;

    @KafkaListener(groupId = "check_stock_response", topics = "check_stock_response" )
    public Boolean rollbackOrderCreate(Boolean isEnough){

//        // 업체에 orderId, receiver, supplier 전송
//        kafkaTemplate.send("order_create","order", CreateOrderKafkaDto.builder()
//                .orderId(order.getId())
//                .supplierId(dto.getSupplierId())
//                .receiverId(dto.getReceiverId())
//                .products(dto.getProducts())
//                .build()
//        );
        return isEnough;
    }
}
