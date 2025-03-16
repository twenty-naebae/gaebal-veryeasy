package com.gaebal_easy.client.user.presentation.dtos;

import lombok.Getter;

@Getter
public class JoinRequest {

    String username;
    String password;
    String slackId;
    String position; //배송담당자, 마스터등
    String name; //이름
    String group; //소속
}
