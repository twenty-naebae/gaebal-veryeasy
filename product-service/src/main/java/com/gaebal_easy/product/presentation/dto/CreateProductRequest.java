package com.gaebal_easy.product.presentation.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateProductRequest {

    private String name;
    private Long price;
    private String description;

}
