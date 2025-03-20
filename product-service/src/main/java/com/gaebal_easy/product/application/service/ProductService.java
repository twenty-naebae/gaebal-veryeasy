package com.gaebal_easy.product.application.service;

import com.gaebal_easy.product.application.dto.ProductResponse;
import com.gaebal_easy.product.domain.entity.Product;
import com.gaebal_easy.product.domain.repository.ProductRepository;
import com.gaebal_easy.product.presentation.dto.CreateProductRequest;
import com.gaebal_easy.product.presentation.dto.SearchProductRequest;
import com.gaebal_easy.product.presentation.dto.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Transactional
    public ProductResponse deleteProduct(UUID productId) {
        Product find = productRepository.findById(productId).orElseThrow();
        // TODO: deltedBY 설정
        find.delete("test deletedby");
        productRepository.save(find);
        return ProductResponse.of(find);
    }

    public Page<Product> searchProduct(SearchProductRequest searchRequest, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> products = productRepository.searchProduct(searchRequest, pageable);

        return products;
    }
}
