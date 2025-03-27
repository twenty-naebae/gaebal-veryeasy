package com.gaebal_easy.product.infrastructure.db;

import com.gaebal_easy.product.domain.entity.Product;
import com.gaebal_easy.product.domain.entity.QProduct;
import com.gaebal_easy.product.presentation.dto.SearchProductRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> searchProduct(SearchProductRequest searchRequest, Pageable pageable) {
        QProduct product = QProduct.product;

        BooleanBuilder builder = new BooleanBuilder();
        if(searchRequest!=null){
            if(searchRequest.getName() != null){
                builder.and(product.name.eq(searchRequest.getName()));
            }

        }

        QueryResults<Product> results = queryFactory
                .selectFrom(product)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.createdAt.desc())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());

    }
}
