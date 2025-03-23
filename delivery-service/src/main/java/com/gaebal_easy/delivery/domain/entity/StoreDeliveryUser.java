package com.gaebal_easy.delivery.domain.entity;

import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_store_delivery_user")
public class StoreDeliveryUser extends BaseTimeEntity {

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer deliveryOrder;


    @Column(nullable = false)
    private Integer hubId;

    public static StoreDeliveryUser of(Long userId, String name, String slackId, String hubId) {
        return StoreDeliveryUser.builder()
                .userId(userId)
                .name(name)
                .slackId(slackId)
                .hubId(Integer.parseInt(hubId))
                .build();
    }
}
