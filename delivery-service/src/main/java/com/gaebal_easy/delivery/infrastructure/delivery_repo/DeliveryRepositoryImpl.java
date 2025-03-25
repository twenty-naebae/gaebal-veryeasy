package com.gaebal_easy.delivery.infrastructure.delivery_repo;

import com.gaebal_easy.delivery.domain.entity.Delivery;
import com.gaebal_easy.delivery.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {

    private final DeliveryJpaRepository deliveryJpaRepository;
    public Optional<Delivery> getDelivery(UUID id) {return deliveryJpaRepository.findById(id);}

    public void save(Delivery delivery) {deliveryJpaRepository.save(delivery);}
}
