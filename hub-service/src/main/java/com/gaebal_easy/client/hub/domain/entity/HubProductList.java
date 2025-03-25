package com.gaebal_easy.client.hub.domain.entity;

import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_hub_product_list")
public class HubProductList extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hub_id")
    private Hub hub;

    private String name;

    private Long price;

    @Column(nullable = false)
    private Long amount;

    private String description;

    private UUID productId;


    public void updateAmount(Long amount) {
        this.amount = amount;
    }

}
