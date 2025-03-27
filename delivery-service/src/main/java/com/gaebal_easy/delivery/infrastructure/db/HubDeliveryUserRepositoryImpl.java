package com.gaebal_easy.delivery.infrastructure.db;

import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;
import com.gaebal_easy.delivery.domain.repository.HubDeliveryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<HubDeliveryUser> findAll(Pageable pageable) {
        return hubDeliveryUserJpaRepository.findAll(pageable);
    }

    @Override
    public void update(HubDeliveryUser hubDeliveryUser,String name, String slackId) {
        hubDeliveryUser.update(name, slackId);
        hubDeliveryUserJpaRepository.save(hubDeliveryUser);
    }
}
