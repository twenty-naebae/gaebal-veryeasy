package com.gaebal_easy.delivery.infrastructure.delivery_repo;

import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreDeliveryUserJpaRepository extends JpaRepository<StoreDeliveryUser, Long> {
}
