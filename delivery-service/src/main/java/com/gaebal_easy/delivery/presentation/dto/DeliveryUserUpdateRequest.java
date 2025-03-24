package com.gaebal_easy.delivery.presentation.dto;

import lombok.Getter;

@Getter
public class DeliveryUserUpdateRequest {

    private String name;
    private String slackId;
    private String hubId;
}
