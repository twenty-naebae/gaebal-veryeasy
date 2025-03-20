package com.gaebal_easy.hub.infrastructure.repository;

import com.gaebal_easy.hub.domain.entity.HubManager;
import com.gaebal_easy.hub.domain.repository.HubManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubManagerImplRepository implements HubManagerRepository {
    private final HubManagerJpaRepository hubManagerJpaRepository;

    @Override
    public HubManager save(HubManager hubManager) {
        return hubManagerJpaRepository.save(hubManager);
    }
}
