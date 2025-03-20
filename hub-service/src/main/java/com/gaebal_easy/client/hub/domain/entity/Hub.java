package com.gaebal_easy.client.hub.domain.entity;

import com.gaebal_easy.client.hub.domain.enums.HubLocation;
import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_hub")
public class Hub extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    private HubLocation hubLocation;

    @OneToMany(mappedBy = "hub", cascade = CascadeType.ALL)
    private final List<HubProductList> hubProductList = new ArrayList<>();

    @OneToMany(mappedBy = "hub", cascade = CascadeType.ALL)
    private final List<HubManager> hubManagers = new ArrayList<>();

    @OneToMany(mappedBy = "hub", cascade = CascadeType.ALL)
    private final List<Hub> connectedHub = new ArrayList<>();

}
