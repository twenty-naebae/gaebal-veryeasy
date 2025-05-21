package com.gaebal_easy.order.domain.repository;

import com.gaebal_easy.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Optional<Order> findById(UUID id);
    Order save(Order order);

}
