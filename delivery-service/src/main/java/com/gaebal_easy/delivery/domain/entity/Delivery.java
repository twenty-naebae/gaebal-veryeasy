package com.gaebal_easy.delivery.domain.entity;

import com.gaebal_easy.delivery.domain.enums.DeliveryStatus;
import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_delivery")
public class Delivery extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID receiverId;

    @Column(nullable = false)
    private UUID supplierId;

    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(nullable = false)
    private String departHubName;

    @Column(nullable = false)
    private String departAddress;

    @Column(nullable = false)
    private String arriveHubName;

    @Column(nullable = false)
    private String arriveAddress;

    private UUID deliveryPersonId;

    private int totalTime;
    private double totalDist;

    public void updateStatus(DeliveryStatus status){
        this.deliveryStatus = status;
    }
}
