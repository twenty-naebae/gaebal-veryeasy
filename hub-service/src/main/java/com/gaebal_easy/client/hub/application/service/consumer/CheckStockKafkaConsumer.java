package com.gaebal_easy.client.hub.application.service.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.hub.application.dto.ProductResponseDto;
import com.gaebal_easy.client.hub.application.dto.checkStockDto.CheckStockDto;
import com.gaebal_easy.client.hub.application.dto.checkStockDto.CheckStockResponse;
import com.gaebal_easy.client.hub.application.dto.checkStockDto.CheckStokProductDto;
import com.gaebal_easy.client.hub.application.dto.checkStockDto.ReservationDto;
import com.gaebal_easy.client.hub.application.service.HubService;
import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.entity.HubProductList;
import com.gaebal_easy.client.hub.domain.entity.Reservation;
import com.gaebal_easy.client.hub.domain.enums.ReservationState;
import com.gaebal_easy.client.hub.domain.repository.HubProductListRepository;
import com.gaebal_easy.client.hub.domain.repository.HubRepository;
import com.gaebal_easy.client.hub.domain.repository.ReservationRepository;
import gaebal_easy.common.global.exception.CanNotFindProductInHubException;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.HubNotFoundException;
import gaebal_easy.common.global.exception.OutOfStockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckStockKafkaConsumer {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String,String> redisTemplate;
    private final ReservationRepository reservationRepository;
    private final KafkaTemplate kafkaTemplate;
    private final HubService hubService;

    @KafkaListener(groupId = "check_stock_hub", topics = "check_stock")
    public void checkStock(String message) {
        try {
            boolean isEnoughStock = true;
            CheckStockDto stockCheckDto = objectMapper.readValue(message, CheckStockDto.class);

            ValueOperations<String, String> ops = redisTemplate.opsForValue();

            ArrayList<ReservationDto> reservations = new ArrayList<>();
            ReservationState stockState = ReservationState.RESERVE;
            UUID reservationId = UUID.randomUUID();

            for(int i=0; i<stockCheckDto.getProducts().size(); i++) {
                CheckStokProductDto product = stockCheckDto.getProducts().get(i);
                UUID productId = product.getProductId();
                String cacheStock = ops.get("stock:" + productId);

                if (cacheStock == null) {
                    // 캐싱 Miss
                    log.info("캐싱 Miss!!!");
                    ProductResponseDto findProduct = hubService.getProduct(productId, stockCheckDto.getHubId());
                    Long stock = findProduct.getAmount();

                    ops.set("stock:" +productId, stock.toString());
                }

                // 예약 생성
                Reservation reservation = Reservation.builder()
                        .reservationId(reservationId)
                        .orderId(stockCheckDto.getOrderId())
                        .productId(productId)
                        .quantity(product.getQuantity())
                        .state(ReservationState.RESERVE)
                        .build();


                // 재고 감소
                Long tempStock = ops.decrement("stock:" + productId.toString(), product.getQuantity());

                log.info("product {}, 재고 {}",productId, tempStock);
                if(tempStock == 0) {
                    log.info("재고 리필");
                    stockState = ReservationState.RE_FILL;
                    reservations.add(ReservationDto.from(reservation));
                    reservation.changeState(ReservationState.RE_FILL);
                }
                // 재고 부족
                else if (tempStock < 0) {
                    log.info("재고 부족");
                    // stock 캐시 롤백
                    for(int j=0; j<i+1;j++){
                        CheckStokProductDto rollbackProduct = stockCheckDto.getProducts().get(j);
                        UUID rollBakcProductId = rollbackProduct.getProductId();

                        ops.increment("stock:" + rollBakcProductId.toString(), rollbackProduct.getQuantity());
                        log.info("stock 캐시 롤백 {} ++{}", rollBakcProductId, rollbackProduct.getQuantity());
                    }

                    // order create 보상 트랜잭션
                    log.info("Order 보상 트랜잭션 요청 {}", stockCheckDto.getOrderId().toString());
                    kafkaTemplate.send("out_of_stock","order-service", stockCheckDto.getOrderId().toString());

                    // reservation 테이블 롤백
                    throw new OutOfStockException(Code.OUT_OF_STOCK);

                }


                reservationRepository.save(reservation);


            }


            kafkaTemplate.send("check_stock_response", "check",objectMapper.writeValueAsString(CheckStockResponse.builder()
                    .reservations(reservations)
                    .state(stockState)
                    .build()));



        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }



}
