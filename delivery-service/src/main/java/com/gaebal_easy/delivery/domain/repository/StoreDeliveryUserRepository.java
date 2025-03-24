package com.gaebal_easy.delivery.domain.repository;

import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StoreDeliveryUserRepository {
    public StoreDeliveryUser save(StoreDeliveryUser storeDeliveryUser);

    Optional<StoreDeliveryUser> findByUserId(Long userId);

    void delete(StoreDeliveryUser storeDeliveryUser, String deletedBy);

    Page<StoreDeliveryUser> findAll(Pageable pageable);
}
