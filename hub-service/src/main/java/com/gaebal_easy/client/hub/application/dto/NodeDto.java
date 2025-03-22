package com.gaebal_easy.client.hub.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NodeDto {
    String name;
    int totalTime;
    Double totalDist;
}
