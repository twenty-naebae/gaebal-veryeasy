package com.gaebal_easy.order.application.dto;

import com.gaebal_easy.order.presentation.dto.CreateOrderRequest;
import com.gaebal_easy.order.presentation.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateOrderDto {
    private UUID hubId;
    private List<ProductDto> products;
    private String orderRequest;
    private String receiver;
    private String supplier;
    private String address;


    public static CreateOrderDto create(UUID hubId, List<ProductDto> products, String orderRequest, String receiver, String supplier, String address) {
        return CreateOrderDto.builder()
                .hubId(hubId)
                .products(products)
                .orderRequest(orderRequest)
                .receiver(receiver)
                .supplier(supplier)
                .address(address).build();
    }
}
