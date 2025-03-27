package com.gaebal_easy.order.application.service.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.order.application.dto.CheckStockResponse;
import com.gaebal_easy.order.application.dto.CreateOrderKafkaDto;
import com.gaebal_easy.order.application.dto.ReservationDto;
import com.gaebal_easy.order.domain.entity.Order;
import com.gaebal_easy.order.domain.enums.ReservationState;
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
    private final ObjectMapper objectMapper;

    @KafkaListener(groupId = "check_stock_response", topics = "check_stock_response" )
    public void rollbackOrderCreate(String message){

        try {
            CheckStockResponse response = objectMapper.readValue(message, CheckStockResponse.class);
            ReservationDto reservationDto = response.getReservations().get(0);
            UUID orderId = reservationDto.getOrderId();

            if(!response.getState().equals(ReservationState.OUT_OF_STOCK)) {
                // 허브에 선점했던 재고 확정 처리 요청. Kafka: Order -> Hub
//                kafkaTemplate.send("confirm_stock", "product_list", dto.getProducts());

                // 업체에 orderId, receiver, supplier 전송. Kafka: Order -> Store
//                kafkaTemplate.send("order_create", "order", CreateOrderKafkaDto.builder()
//                        .orderId(orderId)
//                        .supplierId(dto.getSupplierId())
//                        .receiverId(dto.getReceiverId())
//                        .products(dto.getProducts())
//                        .build());
                if(response.getState().equals(ReservationState.RE_FILL)){
                    kafkaTemplate.send("refill_stock","refill",  objectMapper.writeValueAsString(response));
                }
            }


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


//        // 업체에 orderId, receiver, supplier 전송
//        kafkaTemplate.send("order_create","order", CreateOrderKafkaDto.builder()
//                .orderId(order.getId())
//                .supplierId(dto.getSupplierId())
//                .receiverId(dto.getReceiverId())
//                .products(dto.getProducts())
//                .build()
//        );
//        return isEnough;
    }
}
