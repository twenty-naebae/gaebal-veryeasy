package com.gaebal_easy.product.presentation;

import com.gaebal_easy.product.application.dto.ProductResponse;
import com.gaebal_easy.product.application.service.ProductService;
import com.gaebal_easy.product.domain.entity.Product;
import com.gaebal_easy.product.presentation.dto.CreateProductRequest;
import com.gaebal_easy.product.presentation.dto.SearchProductRequest;
import com.gaebal_easy.product.presentation.dto.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable("productId") UUID productId){
        return ResponseEntity.ok(productService.deleteProduct(productId));
    }

    @GetMapping("/products/search")
    public ResponseEntity<Page<Product>> searchProduct(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc,
            @ModelAttribute SearchProductRequest searchRequest){
        return ResponseEntity.ok(productService.searchProduct(searchRequest, page-1, size, sortBy, isAsc));
    }


}
