package com.gaebal_easy.product.infrastructure;

import com.gaebal_easy.product.domain.entity.Product;
import com.gaebal_easy.product.presentation.dto.SearchProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<Product> searchProduct(SearchProductRequest searchRequest, Pageable pageable);
}
