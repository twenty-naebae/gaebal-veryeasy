package com.gaebal_easy.client.user.presentation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class UserInfoResponse {

    private Long userId;
    private String username;
    private String role;

    public static UserInfoResponse of(Long userId, String username, String role) {
        return UserInfoResponse.builder()
                .userId(userId)
                .username(username)
                .role(role)
                .build();
    }
}
