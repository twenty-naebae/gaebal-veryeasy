package com.gaebal_easy.order.domain.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "p_order_product")
public class OrderProduct {

    @Builder(access = AccessLevel.PROTECTED)
    public OrderProduct(Order order, UUID productId, Long quantity) {
        this.order = order;
        this.productId = productId;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "quantity")
    private Long quantity;

    public void setOrder(Order order) {
        this.order = order;
        if(!order.getOrderProducts().contains(this)) {
            order.getOrderProducts().add(this);
        }
    }

}
