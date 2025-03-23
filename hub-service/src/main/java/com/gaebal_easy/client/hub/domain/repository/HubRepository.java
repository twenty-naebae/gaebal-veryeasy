package com.gaebal_easy.client.hub.domain.repository;

import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.enums.HubLocation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HubRepository {
    Optional<Hub> getHub(UUID id);
    void save(Hub hub);
    Optional<Hub> findByHubLocation(HubLocation hubLocation);

    List<Hub> findAll();
}
