package com.gaebal_easy.hub.infrastructure.repository;

import com.gaebal_easy.hub.domain.entity.HubManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubManagerJpaRepository extends JpaRepository<HubManager, UUID> {
}
