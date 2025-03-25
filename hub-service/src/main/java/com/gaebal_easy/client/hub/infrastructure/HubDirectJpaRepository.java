package com.gaebal_easy.client.hub.infrastructure;

import com.gaebal_easy.client.hub.domain.entity.HubDirectMovementInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubDirectJpaRepository extends JpaRepository<HubDirectMovementInfo, UUID> {
}
