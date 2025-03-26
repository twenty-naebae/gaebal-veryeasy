package com.gaebal_easy.client.hub.domain.entity;

import com.gaebal_easy.client.hub.domain.enums.ReservationState;
import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID reservationId;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private ReservationState state;

    public void changeState(ReservationState state) {
        this.state = state;
    }


}
