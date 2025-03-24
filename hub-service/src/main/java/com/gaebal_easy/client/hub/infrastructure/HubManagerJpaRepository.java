package com.gaebal_easy.client.hub.infrastructure;

import com.gaebal_easy.client.hub.domain.entity.HubManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HubManagerJpaRepository extends JpaRepository<HubManager, UUID> {
    Optional<HubManager> findByUserId(Long userId);
}
