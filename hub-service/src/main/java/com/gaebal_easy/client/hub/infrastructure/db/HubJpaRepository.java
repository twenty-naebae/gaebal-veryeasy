package com.gaebal_easy.client.hub.infrastructure.db;

import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.enums.HubLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HubJpaRepository extends JpaRepository<Hub, UUID> {

    Optional<Hub> findByHubLocation(HubLocation hubLocation);
}
