package com.gaebal_easy.order.infrastructure.adapter.in;

import com.gaebal_easy.order.application.service.consumer.RollbackOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RollbackOrderKafkaConsumer {

    private final RollbackOrderService rollbackOrderService;

    @KafkaListener(groupId = "err_order_response", topics = "out_of_stock" )
    public void rollbackOrderCreate(String orderId){
        UUID uuid = UUID.fromString(orderId);

        rollbackOrderService.rollbackOrderCreate(uuid);
    }

}
