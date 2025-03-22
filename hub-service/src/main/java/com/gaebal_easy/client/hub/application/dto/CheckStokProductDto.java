package com.gaebal_easy.client.hub.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CheckStokProductDto implements Serializable {

    private UUID productId;
    private Long quantity;
}
