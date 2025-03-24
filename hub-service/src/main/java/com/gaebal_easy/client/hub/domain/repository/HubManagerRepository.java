package com.gaebal_easy.client.hub.domain.repository;


import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.entity.HubManager;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface HubManagerRepository {
    public HubManager save(HubManager hubManager);
    public Optional<HubManager> findByUserId(Long userId);
    public void update(HubManager hubManager, String name, Hub newHub);
    public void delete(HubManager hubManager, String deletedBy);
    public List<HubManager> findAllByFilter(Long hubId, Sort sortType);
}
