package com.gaebal_easy.hub.domain.entity;

import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

// 회원가입 테스트하기위해 가짜 Hub Entity입니다.
@Entity
@Getter
@Table(name="p_hub_temp")
public class HubTemp extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private UUID id;

    private String hubName;
}
