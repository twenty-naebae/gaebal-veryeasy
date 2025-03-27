package com.gaebal_easy.delivery.infrastructure.db;

import com.gaebal_easy.delivery.domain.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryJpaRepository extends JpaRepository<Delivery, UUID> {
}
