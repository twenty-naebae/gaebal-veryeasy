package com.gaebal_easy.delivery.application.dto;

import com.gaebal_easy.delivery.domain.entity.DeliveryDetail;
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
public class DeliveryDetailDto {
    private DeliveryStatus status;
    private UUID orderId;
    private int sequence;
    private String departHubName;
    private String arriveHubName;
    private int expectedTime;
    private double expectedDistance;
    private int realTime;
    private double realDistance;
    private UUID hubDeliveryPersonId;
    public static DeliveryDetailDto of(DeliveryDetail deliveryDetail) {
        return DeliveryDetailDto.builder()
                .status(deliveryDetail.getDeliveryStatus())
                .orderId(deliveryDetail.getOrderId())
                .sequence(deliveryDetail.getSequence())
                .departHubName(deliveryDetail.getDepartHubName())
                .arriveHubName(deliveryDetail.getArriveHubName())
                .expectedTime(deliveryDetail.getExpectedTime())
                .expectedDistance(deliveryDetail.getExpectedDistance())
                .realTime(deliveryDetail.getRealTime())
                .realDistance(deliveryDetail.getRealDistance())
                .hubDeliveryPersonId(deliveryDetail.getHubDeliveryPersonId())
                .build();
    }
}
