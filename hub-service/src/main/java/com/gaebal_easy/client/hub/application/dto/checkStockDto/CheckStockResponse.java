package com.gaebal_easy.client.hub.application.dto.checkStockDto;

import com.gaebal_easy.client.hub.domain.enums.ReservationState;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CheckStockResponse {

    private List<ReservationDto> reservations;
    private ReservationState state;

}
