package com.gaebal_easy.delivery.domain.entity;

import com.gaebal_easy.delivery.application.dto.kafkaConsumerDto.KafkaRequireAddressToHubDto;
import com.gaebal_easy.delivery.application.feign.HubDirectDto;
import com.gaebal_easy.delivery.application.feign.HubRouteDto;
import com.gaebal_easy.delivery.domain.enums.DeliveryStatus;
import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_delivery_route")
public class DeliveryDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private String departHubName;

    @Column(nullable = false)
    private String arriveHubName;

    private int sequence;

    private int expectedTime;
    private double expectedDistance;

    private int realTime;
    private double realDistance;

    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private UUID hubDeliveryPersonId;
    public void updateStatus(DeliveryStatus status){
        this.deliveryStatus = status;
    }

    public static DeliveryDetail of(HubDirectDto realHubDirectDto, HubDirectDto expectedHubDirectDto, int seq, UUID orderId) {
        return DeliveryDetail.builder()
                .orderId(orderId)
                .departHubName(realHubDirectDto.getDepartName())
                .arriveHubName(realHubDirectDto.getArriveName())
                .expectedTime(expectedHubDirectDto.getTime())
                .expectedDistance(expectedHubDirectDto.getDistance())
                .realTime(realHubDirectDto.getTime())
                .realDistance(realHubDirectDto.getDistance())
                .sequence(seq)
                .deliveryStatus(DeliveryStatus.MOVE_TO_HUB)
                // TODO : hubDeliveryPersonID 넣어야함
                .build();
    }
}
