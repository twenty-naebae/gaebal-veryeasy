package com.gaebal_easy.order.presentation.dto;

import com.gaebal_easy.order.application.dto.CreateOrderDto;
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
public class CreateOrderRequest {

    private List<ProductDto> products;
    private String orderRequest;
    private String receiver;
    private String supplier;
    private String address;


    public CreateOrderDto toDto() {
        return CreateOrderDto.create(this.products, this.orderRequest, this.receiver, this.supplier, this.address);
    }

}
