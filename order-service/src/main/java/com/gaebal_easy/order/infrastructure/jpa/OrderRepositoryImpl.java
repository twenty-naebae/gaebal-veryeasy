package com.gaebal_easy.order.infrastructure.jpa;

import com.gaebal_easy.order.domain.entity.Order;
import com.gaebal_easy.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;


    @Override
    public Optional<Order> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Order save(Order order) {
        return jpaRepository.save(order);
    }
}
