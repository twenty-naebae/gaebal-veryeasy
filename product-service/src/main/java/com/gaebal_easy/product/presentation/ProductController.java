package com.gaebal_easy.product.presentation;

import com.gaebal_easy.product.application.dto.ProductResponse;
import com.gaebal_easy.product.application.service.ProductService;
import com.gaebal_easy.product.presentation.dto.CreateProductRequest;
import com.gaebal_easy.product.presentation.dto.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/product-service/api")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest createProductRequest) {
        log.info("createProductRequest: {}", createProductRequest);
        return ResponseEntity.ok(productService.createProduct(createProductRequest));
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") UUID productId){
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @PatchMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable("productId") UUID productId, @RequestBody UpdateProductRequest updateRequest){
        return ResponseEntity.ok(productService.updateProduct(productId, updateRequest));
    }


}
