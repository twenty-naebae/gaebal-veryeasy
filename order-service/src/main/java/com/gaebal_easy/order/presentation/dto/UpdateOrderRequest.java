package com.gaebal_easy.order.presentation.dto;

import com.gaebal_easy.order.application.dto.UpdateOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest {

    private String address;


    public UpdateOrderDto toDto() {
        return UpdateOrderDto.create(this.address);
    }


}
