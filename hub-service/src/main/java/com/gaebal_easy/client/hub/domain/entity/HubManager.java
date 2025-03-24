package com.gaebal_easy.client.hub.domain.entity;

import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_hub_manager")
public class HubManager extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column
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

    public void update(String name, Hub newHub) {
        this.name = name;
        this.hub = newHub;
    }

}
