package com.gaebal_easy.delivery.domain.repository;

import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;

import java.util.Optional;

public interface StoreDeliveryUserRepository {
    public StoreDeliveryUser save(StoreDeliveryUser storeDeliveryUser);

    Optional<StoreDeliveryUser> findByUserId(Long userId);
}
