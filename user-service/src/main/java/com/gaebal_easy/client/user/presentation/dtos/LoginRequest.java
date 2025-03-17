package com.gaebal_easy.client.user.presentation.dtos;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String password;
}