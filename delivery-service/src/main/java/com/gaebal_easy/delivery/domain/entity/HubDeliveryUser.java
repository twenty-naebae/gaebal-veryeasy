package com.gaebal_easy.delivery.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_hub_delivery_user")
public class HubDeliveryUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slackId;

    // 배송 순번
    @Column(nullable = false, unique = true)
    private Integer deliveryOrder;

    public static HubDeliveryUser of(Long userId, String name, String slackId,Integer deliveryOrder) {
        return HubDeliveryUser.builder()
                .userId(userId)
                .name(name)
                .slackId(slackId)
                .deliveryOrder(deliveryOrder)
                .build();
    }
}
