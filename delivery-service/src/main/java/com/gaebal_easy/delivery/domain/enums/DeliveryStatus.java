package com.gaebal_easy.delivery.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryStatus {
    WAITING_HUB,MOVE_TO_HUB,ARRIVE_HUB,DELIVERING,COMPLETE
}
