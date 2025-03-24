package com.gaebal_easy.delivery.domain.repository;

import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;

import java.util.Optional;

public interface HubDeliveryUserRepository {
    public HubDeliveryUser save(HubDeliveryUser hubDeliveryUser);

    Optional<HubDeliveryUser> findByUserId(Long userId);

    void delete(HubDeliveryUser hubDeliveryUser, String deletedBy);
}
