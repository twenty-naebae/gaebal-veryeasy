package com.gaebal_easy.client.hub.domain.entity;

import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Table(name = "p_hub_movement_info")
public class HubMovementInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false)
    private UUID depart;

    @Column(nullable = false)
    private UUID arrive;

    @Column(nullable = false)
    private int totalRequiredTime;

    @Column(nullable = false)
    private double totalDistance;

    @OneToMany(mappedBy = "hubMovementInfo", cascade = CascadeType.ALL)
    private final List<VisitHub> visitHubs = new ArrayList<>();
}
