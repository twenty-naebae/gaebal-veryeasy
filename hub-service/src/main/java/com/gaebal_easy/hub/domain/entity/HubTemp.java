package com.gaebal_easy.hub.domain.entity;

import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

// 회원가입 테스트하기위해 가짜 Hub Entity입니다.
@Entity
@Table(name="p_hub_temp")
public class HubTemp extends BaseTimeEntity{

    @Id
    @UuidGenerator
    @Column
    private UUID id;

    private String hubName;
}
