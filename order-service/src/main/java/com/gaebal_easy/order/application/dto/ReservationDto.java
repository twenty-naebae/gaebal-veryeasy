package com.gaebal_easy.order.application.dto;


import com.gaebal_easy.order.domain.enums.ReservationState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    private UUID reservationId;
    private UUID orderId;
    private UUID productId;
    private Long quantity;
    private ReservationState state;


}
