package com.gaebal_easy.delivery.infrastructure.delivery_repo;

import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;
import com.gaebal_easy.delivery.domain.repository.HubDeliveryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HubDeliveryUserRepositoryImpl implements HubDeliveryUserRepository {

    private final HubDeliveryUserJpaRepository hubDeliveryUserJpaRepository;

    @Override
    public HubDeliveryUser save(HubDeliveryUser hubDeliveryUser) {
        return hubDeliveryUserJpaRepository.save(hubDeliveryUser);
    }

    @Override
    public Optional<HubDeliveryUser> findByUserId(Long userId) {
        return hubDeliveryUserJpaRepository.findByUserId(userId);
    }

    @Override
    public void delete(HubDeliveryUser hubDeliveryUser, String deletedBy) {
        hubDeliveryUser.delete(deletedBy);
        hubDeliveryUserJpaRepository.save(hubDeliveryUser);
    }
}
