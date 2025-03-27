package com.gaebal_easy.delivery.infrastructure.db;

import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;
import com.gaebal_easy.delivery.domain.repository.StoreDeliveryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StoreDeliveryUserRepositoryImpl implements StoreDeliveryUserRepository {

    private final StoreDeliveryUserJpaRepository storeDeliveryUserJpaRepository;

    @Override
    public StoreDeliveryUser save(StoreDeliveryUser storeDeliveryUser) {
        return storeDeliveryUserJpaRepository.save(storeDeliveryUser);
    }

    @Override
    public Optional<StoreDeliveryUser> findByUserId(Long userId) {
        return storeDeliveryUserJpaRepository.findByUserId(userId);
    }

    @Override
    public void delete(StoreDeliveryUser storeDeliveryUser, String deletedBy) {
        storeDeliveryUser.delete(deletedBy);
        storeDeliveryUserJpaRepository.save(storeDeliveryUser);
    }

    @Override
    public Page<StoreDeliveryUser> findAll(Pageable pageable) {
        return storeDeliveryUserJpaRepository.findAll(pageable);
    }

    @Override
    public void update(StoreDeliveryUser storeDeliveryUser, String name, String slackId, String hubId) {
        storeDeliveryUser.update(name, slackId, hubId);
        storeDeliveryUserJpaRepository.save(storeDeliveryUser);
    }
}
