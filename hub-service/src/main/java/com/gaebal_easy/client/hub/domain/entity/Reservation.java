package com.gaebal_easy.client.hub.domain.entity;

import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation extends BaseTimeEntity {

    @Id
    private UUID id;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "quantity")
    private Long quantity;




}
