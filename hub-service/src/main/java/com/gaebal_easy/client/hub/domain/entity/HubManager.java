package com.gaebal_easy.client.hub.domain.entity;

import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_hub_manager")
public class HubManager extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Long userId;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id")
    private Hub hub;

    public static HubManager of(Long userId, String name, Hub hub) {
        return HubManager.builder()
                .userId(userId)
                .name(name)
                .hub(hub)
                .build();
    }

}
