package com.gaebal_easy.delivery.infrastructure.delivery_repo;

import com.gaebal_easy.delivery.domain.entity.DeliveryDetail;
import com.gaebal_easy.delivery.domain.repository.DeliveryDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryDetailRepositoryImpl implements DeliveryDetailRepository {

    private final DeliveryDetailJpaRepository deliveryDetailJpaRepository;
    @Override
    public Optional<DeliveryDetail> getDetailDelivery(UUID id) {
        return deliveryDetailJpaRepository.findById(id);
    }

    @Override
    public void save(DeliveryDetail deliveryDetail) {
        deliveryDetailJpaRepository.save(deliveryDetail);
    }
}
