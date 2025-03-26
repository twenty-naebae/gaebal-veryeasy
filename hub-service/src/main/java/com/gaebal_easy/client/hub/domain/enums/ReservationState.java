package com.gaebal_easy.client.hub.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReservationState {
    RESERVE("예약"), OUT_OF_STOCK("부족"), RE_FILL("리필");

    private final String value;
}
