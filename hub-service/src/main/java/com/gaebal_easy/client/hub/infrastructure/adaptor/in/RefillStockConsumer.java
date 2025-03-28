package com.gaebal_easy.client.hub.infrastructure.adaptor.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.hub.application.dto.checkStockDto.CheckStockResponse;
import com.gaebal_easy.client.hub.application.dto.checkStockDto.ReservationDto;
import com.gaebal_easy.client.hub.application.service.consumer.RefillStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RefillStockConsumer {

    private final ObjectMapper objectMapper;
    private final RefillStockService refillStockService;

    @KafkaListener(groupId = "refill", topics = "refill_stock")
    public void refillStock(String message){
        try {
            CheckStockResponse checkStockResponse = objectMapper.readValue(message, CheckStockResponse.class);
            List<ReservationDto> reservations = checkStockResponse.getReservations();

            refillStockService.refillStock(reservations);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
