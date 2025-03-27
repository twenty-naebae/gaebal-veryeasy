package com.gaebal_easy.client.hub.application.dto.checkStockDto;

import com.gaebal_easy.client.hub.domain.entity.Reservation;
import com.gaebal_easy.client.hub.domain.enums.ReservationState;
import jakarta.persistence.*;
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

    public static ReservationDto from(Reservation reservation) {
        return ReservationDto.builder()
                .reservationId(reservation.getReservationId())
                .orderId(reservation.getOrderId())
                .productId(reservation.getProductId())
                .quantity(reservation.getQuantity())
                .state(reservation.getState())
                .build();
    }

}
