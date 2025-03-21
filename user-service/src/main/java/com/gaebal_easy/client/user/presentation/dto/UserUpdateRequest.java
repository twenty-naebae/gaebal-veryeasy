package com.gaebal_easy.client.user.presentation.dto;

import gaebal_easy.common.global.enums.Role;
import lombok.Getter;

@Getter
public class UserUpdateRequest {

    String username;
    String password;
    String slackId;
    String name; //이름
    String group; //소속

}
