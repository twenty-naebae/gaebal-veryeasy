package com.gaebal_easy.order.application.dto;


import com.gaebal_easy.order.domain.enums.ReservationState;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CheckStockResponse {

    private List<ReservationDto> reservations;
    private ReservationState state;

}
