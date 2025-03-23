package com.gaebal_easy.delivery.application.dto;

import com.gaebal_easy.delivery.domain.entity.Delivery;
import com.gaebal_easy.delivery.domain.enums.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class DeliveryDto {
    private DeliveryStatus status;
    private String departHubName;
    private String arriveHubName;
    private String receiver;
    private UUID deliveryPersonId;
    private int totalTime;
    private double totalDist;
    public static DeliveryDto of(Delivery delivery) {
        return DeliveryDto.builder()
                .status(delivery.getDeliveryStatus())
                .departHubName(delivery.getDepartHubName())
                .arriveHubName(delivery.getArriveHubName())
                .receiver(delivery.getArriveAddress())
                .deliveryPersonId(delivery.getDeliveryPersonId())
                .totalTime(delivery.getTotalTime())
                .totalDist(delivery.getTotalDist())
                .build();
    }
}
