package com.gaebal_easy.product.domain.repository;

import com.gaebal_easy.product.domain.entity.Product;
import com.gaebal_easy.product.infrastructure.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {
}
