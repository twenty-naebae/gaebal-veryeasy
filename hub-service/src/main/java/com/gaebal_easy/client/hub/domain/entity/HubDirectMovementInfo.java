package com.gaebal_easy.client.hub.domain.entity;

import com.gaebal_easy.client.hub.domain.enums.HubLocation;
import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Table(name = "p_hub_direct_movement_info")
public class HubDirectMovementInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String depart;

    @Column(nullable = false)
    private String arrive;

    @Column(nullable = true)
    private int requiredTime;

    @Column(nullable = true)
    private double distance;


}
