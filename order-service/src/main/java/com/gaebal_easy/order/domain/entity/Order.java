package com.gaebal_easy.order.domain.entity;


import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SQLRestriction("is_deleted = false")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "p_order")
public class Order extends BaseTimeEntity {

    @Builder(access = AccessLevel.PROTECTED)
    public Order(String supplier, String receiver, String orderRequest, Long totalPrice) {
        this.supplier = supplier;
        this.receiver = receiver;
        this.orderRequest = orderRequest;
        this.totalPrice = totalPrice;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "supplier", nullable = false)
    private String supplier;
    @Column(name = "recevier", nullable = false)
    private String receiver;
    @Column(name = "order_request")
    private String orderRequest;
    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderProduct> orderProducts = new ArrayList<>();


    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        if(orderProduct.getOrder() != null) {
            orderProduct.setOrder(this);
        }
    }






}
