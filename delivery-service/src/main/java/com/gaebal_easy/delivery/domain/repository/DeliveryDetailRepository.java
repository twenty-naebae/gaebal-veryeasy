package com.gaebal_easy.delivery.domain.repository;

import com.gaebal_easy.delivery.domain.entity.DeliveryDetail;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryDetailRepository {
    Optional<DeliveryDetail> getDetailDelivery(UUID id);
    void save(DeliveryDetail deliveryDetail);
}
