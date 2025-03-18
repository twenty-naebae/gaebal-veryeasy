package com.gaebal_easy.client.user.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String refresh;

    private String expiration;

    public RefreshToken(Long userId, String refreshTokenValue, Long expiredMs){
        this.userId = userId;
        this.refresh = refreshTokenValue;
        this.expiration = (new Date(System.currentTimeMillis() + expiredMs)).toString();
    }
}
