package com.gaebal_easy.client.user.domain.entity;

import gaebal_easy.common.global.enums.Role;
import com.gaebal_easy.client.user.presentation.dto.JoinRequest;
import gaebal_easy.common.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@SQLRestriction("is_deleted = false")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(JoinRequest joinRequest){
        this.username = joinRequest.getUsername();
        this.password = joinRequest.getPassword();
        this.role = joinRequest.getRole();
    }

    public void update(String username, String newPassword) {
        this.username = username;
        this.password = newPassword;
    }
}
