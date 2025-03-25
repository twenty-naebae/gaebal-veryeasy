package com.gaebal_easy.order.application.service;

import com.gaebal_easy.order.application.dto.CreateOrderDto;
import com.gaebal_easy.order.application.dto.CreateOrderKafkaDto;
import com.gaebal_easy.order.application.dto.OrderResponse;
import com.gaebal_easy.order.application.dto.UpdateOrderDto;
import com.gaebal_easy.order.application.service.kafka.consumer.CheckStockResponseConsumer;
import com.gaebal_easy.order.application.service.kafka.consumer.RollbackOrderKafkaConsumer;
import com.gaebal_easy.order.domain.entity.Order;
import com.gaebal_easy.order.domain.entity.OrderProduct;
import com.gaebal_easy.order.domain.repository.OrderRepository;
import com.gaebal_easy.order.infrastructure.kafka.producer.CheckStockKafkaProducer;
import com.gaebal_easy.order.presentation.dto.ProductRequestDto;
import com.gaebal_easy.order.application.dto.CheckStockDto;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.OrderFailExceiption;
import gaebal_easy.common.global.exception.OutOfStockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate kafkaTemplate;
    private final CheckStockKafkaProducer stockCheckKafkaProducer;
    private final RollbackOrderKafkaConsumer kafkaConsumer;
    private final CheckStockResponseConsumer checkStockResponseConsumer;

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

    // TODO: deleteOrder
    public OrderResponse deleteOrder(UUID orderId) {
        return null;
    }

    public OrderResponse createOrder(CreateOrderDto dto)  {
        // 주문 생성
        Long totalPrice = calculateTotalPrice(dto);
        Order order = Order.create(dto.getSupplierId(), dto.getReceiverId(), dto.getOrderRequest(), dto.getAddress(), totalPrice);

        for(ProductRequestDto product: dto.getProducts()){
            OrderProduct orderProduct = OrderProduct.create(product.getProductId(), product.getQuantity());
            order.addOrderProduct(orderProduct);
        }
        orderRepository.save(order);


        // 재고확인 Kafka order -> hub
//        stockCheckKafkaProducer.checkStock(CheckStockDto.builder()
//                        .hubId(dto.getHubId())
//                        .orderId(order.getId())
//                        .products(dto.getProducts())
//                        .build());

        // 재고확인 FeignClient order -> hub
        Boolean isEnough = true;
        try {
             isEnough = (Boolean) hubClient.checkStock(CheckStockDto.builder()
                    .hubId(dto.getHubId())
                    .orderId(order.getId())
                    .products(dto.getProducts())
                    .build()).getBody();

            log.info("재고 확인 결과: {}", isEnough);
            if(isEnough) {
                // 허브에 선점했던 재고 확정 처리 요청
                kafkaTemplate.send("confirm_stock", "product_list", dto.getProducts());

                // 업체에 orderId, receiver, supplier 전송
                kafkaTemplate.send("order_create", "order", CreateOrderKafkaDto.builder()
                        .orderId(order.getId())
                        .supplierId(dto.getSupplierId())
                        .receiverId(dto.getReceiverId())
                        .products(dto.getProducts())
                        .build());
            }

        }catch(Exception e){
            kafkaTemplate.send("refill_stock", "product_list", dto.getProducts());
            throw new OrderFailExceiption(Code.ORDER_FAIL_EXCEIPTION);
        }
        return OrderResponse.from(order);
    }

    private static Long calculateTotalPrice(CreateOrderDto dto) {
        Long totalPrice = 0L;
        for(ProductRequestDto product : dto.getProducts()){
            totalPrice+=(product.getPrice() * product.getQuantity());
        }
        return totalPrice;
    }


    public Boolean checkStock(CreateOrderDto dto) {
        CheckStockDto stockCheck = CheckStockDto.builder()
                .products(dto.getProducts())
                .hubId(dto.getHubId())
                .build();

        ResponseEntity<?> responseEntity = hubClient.checkStock(stockCheck);
        log.info("Check stock: {}", responseEntity.getBody());

        return (Boolean) responseEntity.getBody();
    }


}
