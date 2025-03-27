package com.gaebal_easy.client.hub.infrastructure.db;

import com.gaebal_easy.client.hub.domain.entity.HubManager;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubManagerJpaRepository extends JpaRepository<HubManager, UUID> {
    Optional<HubManager> findByUserId(Long userId);

    @Query("SELECT h FROM HubManager h WHERE (:hubId IS NULL OR h.hub.id = :hubId)")
    List<HubManager> findAllByFilter(Long hubId, Sort sortType);
}
