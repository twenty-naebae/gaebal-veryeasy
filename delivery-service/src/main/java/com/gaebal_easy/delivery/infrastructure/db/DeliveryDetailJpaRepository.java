package com.gaebal_easy.delivery.infrastructure.db;

import com.gaebal_easy.delivery.domain.entity.DeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryDetailJpaRepository extends JpaRepository<DeliveryDetail, UUID> {
}
