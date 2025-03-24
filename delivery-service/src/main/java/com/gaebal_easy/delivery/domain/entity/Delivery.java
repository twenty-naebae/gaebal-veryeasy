package com.gaebal_easy.delivery.domain.entity;

import com.gaebal_easy.delivery.application.dto.kafkaConsumerDto.KafkaRequireAddressToHubDto;
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
@Table(name = "p_delivery")
public class Delivery extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(nullable = false)
    private String departHubName;

    @Column(nullable = false)
    private String arriveStoreName;

    @Column(nullable = false)
    private String arriveHubName;

    @Column(nullable = false)
    private String arriveAddress;

    private UUID deliveryPersonId;

    private int totalTime;
    private double totalDist;

    public void updateStatus(DeliveryStatus status) {
        this.deliveryStatus = status;
    }

    public static Delivery of(HubRouteDto hubRouteDto, KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto) {
        return Delivery.builder()
                .orderId(kafkaRequireAddressToHubDto.getOrderId())
                .deliveryStatus(DeliveryStatus.MOVE_TO_HUB)
                .departHubName(kafkaRequireAddressToHubDto.getSupplyStoreHubName())
                .arriveStoreName(kafkaRequireAddressToHubDto.getReceiptStoreName())
                .arriveHubName(kafkaRequireAddressToHubDto.getReceiptStoreHubName())
                .arriveAddress(kafkaRequireAddressToHubDto.getReceiptStoreAddress())
                // TODO : 여기에 deliveryPersonId 들어가야함
                .totalTime(hubRouteDto.getTotalRequiredTime())
                .totalDist(hubRouteDto.getTotalDistance())
                .build();
    }
}
