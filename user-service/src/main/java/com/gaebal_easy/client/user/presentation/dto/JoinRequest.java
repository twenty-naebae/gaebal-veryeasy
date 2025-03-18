package com.gaebal_easy.client.user.presentation.dto;

import gaebal_easy.common.global.enums.Role;
import lombok.Getter;

@Getter
public class JoinRequest {

    String username;
    String password;
    String slackId;
    Role role; //배송담당자, 마스터등
    String name; //이름
    String group; //소속

    public void setPassword(String password) {
        this.password = password;
    }
}
