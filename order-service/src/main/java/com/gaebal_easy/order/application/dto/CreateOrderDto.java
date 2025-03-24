package com.gaebal_easy.order.application.dto;

import com.gaebal_easy.order.presentation.dto.ProductRequestDto;
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
    private List<ProductRequestDto> products;
    private String orderRequest;
    private UUID receiverId;
    private UUID supplierId;
    private String address;


    public static CreateOrderDto create(UUID hubId, List<ProductRequestDto> products, String orderRequest, UUID receiverId, UUID supplierId, String address) {
        return CreateOrderDto.builder()
                .hubId(hubId)
                .products(products)
                .orderRequest(orderRequest)
                .receiverId(receiverId)
                .supplierId(supplierId)
                .address(address).build();
    }
}
