package com.gaebal_easy.delivery.infrastructure.delivery_repo;

import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;
import com.gaebal_easy.delivery.domain.repository.HubDeliveryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubDeliveryUserRepositoryImpl implements HubDeliveryUserRepository {

    private final HubDeliveryUserJpaRepository hubDeliveryUserJpaRepository;

    @Override
    public HubDeliveryUser save(HubDeliveryUser hubDeliveryUser) {
        return hubDeliveryUserJpaRepository.save(hubDeliveryUser);
    }
}
