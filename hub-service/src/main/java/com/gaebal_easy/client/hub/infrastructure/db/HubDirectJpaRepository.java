package com.gaebal_easy.client.hub.infrastructure.db;

import com.gaebal_easy.client.hub.domain.entity.HubDirectMovementInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubDirectJpaRepository extends JpaRepository<HubDirectMovementInfo, UUID> {
}
