package com.gaebal_easy.client.user.presentation.dto;

import brave.internal.Nullable;
import gaebal_easy.common.global.enums.Role;
import lombok.Getter;

@Getter
public class UserUpdateRequest {

    @Nullable
    String username;

    @Nullable
    String password;

}
