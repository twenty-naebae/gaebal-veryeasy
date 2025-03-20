package com.gaebal_easy.order.application.dto;

import com.gaebal_easy.order.presentation.dto.CreateOrderRequest;
import com.gaebal_easy.order.presentation.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateOrderDto {
    private List<ProductDto> products;
    private String orderRequest;
    private String receiver;
    private String supplier;
    private String address;


    public static CreateOrderDto create(List<ProductDto> products, String orderRequest, String receiver, String supplier, String address) {
        return CreateOrderDto.builder()
                .products(products)
                .orderRequest(orderRequest)
                .receiver(receiver)
                .supplier(supplier)
                .address(address).build();
    }
}
