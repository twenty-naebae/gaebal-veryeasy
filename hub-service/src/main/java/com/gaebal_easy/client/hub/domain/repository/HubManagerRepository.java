package com.gaebal_easy.client.hub.domain.repository;


import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.entity.HubManager;

import java.util.Optional;

public interface HubManagerRepository {
    public HubManager save(HubManager hubManager);
    public Optional<HubManager> findByUserId(Long userId);
    public void update(HubManager hubManager, String name, Hub newHub);
}
