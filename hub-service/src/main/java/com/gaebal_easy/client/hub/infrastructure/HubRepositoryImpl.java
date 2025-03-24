package com.gaebal_easy.client.hub.infrastructure;

import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.enums.HubLocation;
import com.gaebal_easy.client.hub.domain.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

    private final HubJpaRepository hubJpaRepository;

    public Optional<Hub> getHub(UUID id) {
        return hubJpaRepository.findById(id);
    }

    public void save(Hub hub) {hubJpaRepository.save(hub);}

    @Override
    public Optional<Hub> findByHubLocation(HubLocation hubLocation) {
        return hubJpaRepository.findByHubLocation(hubLocation);
    }

    public List<Hub> findAll() {return hubJpaRepository.findAll();}

    @Override
    public Optional<Hub> findById(UUID hubId) {
        return hubJpaRepository.findById(hubId);
    }
}
