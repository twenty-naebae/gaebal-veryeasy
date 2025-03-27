package com.gaebal_easy.client.hub.infrastructure.db;


import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.entity.HubManager;
import com.gaebal_easy.client.hub.domain.repository.HubManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HubManagerImplRepository implements HubManagerRepository {
    private final HubManagerJpaRepository hubManagerJpaRepository;

    @Override
    public HubManager save(HubManager hubManager) {
        return hubManagerJpaRepository.save(hubManager);
    }

    @Override
    public Optional<HubManager> findByUserId(Long id) {
        return hubManagerJpaRepository.findByUserId(id);
    }

    @Override
    public void update(HubManager hubManager, String name, Hub newHub) {
        hubManager.update(hubManager.getName(), newHub);
    }

    @Override
    public void delete(HubManager hubManager, String deletedBy) {
        hubManager.delete(deletedBy);
        hubManagerJpaRepository.save(hubManager);
    }

    @Override
    public List<HubManager> findAllByFilter(Long hubId, Sort sortType) {
        return hubManagerJpaRepository.findAllByFilter(hubId, sortType);
    }
}
