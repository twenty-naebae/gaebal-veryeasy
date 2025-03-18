package com.gaebal_easy.product.application.service;

import com.gaebal_easy.product.application.dto.ProductDto;
import com.gaebal_easy.product.application.dto.ProductResponse;
import com.gaebal_easy.product.domain.entity.Product;
import com.gaebal_easy.product.domain.repository.ProductRepository;
import com.gaebal_easy.product.presentation.dto.CreateProductRequest;
import com.gaebal_easy.product.presentation.dto.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    @Transactional
    public ProductResponse createProduct(CreateProductRequest createProductRequest) {
        Product product = Product.builder()
                .name(createProductRequest.getName())
                .description(createProductRequest.getDescription())
                .price(createProductRequest.getPrice())
                .build();
        productRepository.save(product);

        return ProductResponse.of(product);
    }

    public ProductResponse getProduct(UUID id) {
        return productRepository.findById(id).map(ProductResponse::of).orElseThrow();
    }

    @Transactional
    public ProductResponse updateProduct(UUID productId, UpdateProductRequest updateRequest) {
        Product find = productRepository.findById(productId).orElseThrow();
        find.update(updateRequest.getName(), updateRequest.getPrice(), updateRequest.getDescription());
        productRepository.save(find);
        return ProductResponse.of(find);
    }
}
