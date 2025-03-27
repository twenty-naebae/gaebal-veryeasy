package com.gaebal_easy.delivery.infrastructure.db;

import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreDeliveryUserJpaRepository extends JpaRepository<StoreDeliveryUser, Long> {
    Optional<StoreDeliveryUser> findByUserId(Long userId);
}
