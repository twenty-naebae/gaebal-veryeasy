package com.gaebal_easy.product.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-service/api")
public class ProductController {


    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
