package com.gaebal_easy.delivery.domain.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface HubDeliveryUserRepository {
    public HubDeliveryUser save(HubDeliveryUser hubDeliveryUser);

    Optional<HubDeliveryUser> findByUserId(Long userId);

    void delete(HubDeliveryUser hubDeliveryUser, String deletedBy);

    Page<HubDeliveryUser> findAll(Pageable pageable);
}
