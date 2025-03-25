package com.gaebal_easy.delivery.domain.repository;

import com.gaebal_easy.delivery.application.dto.DeliveryDto;
import com.gaebal_easy.delivery.domain.entity.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository {
    Optional<Delivery> getDelivery(UUID id);

    void save(Delivery delivery);
}
