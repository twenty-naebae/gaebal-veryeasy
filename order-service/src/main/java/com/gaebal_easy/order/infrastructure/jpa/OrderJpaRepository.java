package com.gaebal_easy.order.infrastructure.jpa;

import com.gaebal_easy.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<Order, UUID> {
}
