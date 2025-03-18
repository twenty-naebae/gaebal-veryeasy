package com.gaebal_easy.product.presentation;

import com.gaebal_easy.product.application.dto.ProductResponse;
import com.gaebal_easy.product.application.service.ProductService;
import com.gaebal_easy.product.presentation.dto.CreateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product-service/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest createProductRequest) {

        return ResponseEntity.ok(productService.createProduct(createProductRequest));
    }
}
