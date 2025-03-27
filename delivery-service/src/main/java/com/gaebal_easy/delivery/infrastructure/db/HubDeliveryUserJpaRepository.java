package com.gaebal_easy.delivery.infrastructure.delivery_repo;

import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HubDeliveryUserJpaRepository extends JpaRepository<HubDeliveryUser, Long> {
    Optional<HubDeliveryUser> findByUserId(Long userId);
}
