package com.gaebal_easy.client.hub.domain.repository;

import com.gaebal_easy.client.hub.domain.entity.HubDirectMovementInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HubDirectRepository {
    List<HubDirectMovementInfo> findAll();
    void save(HubDirectMovementInfo hubDirectMovementInfo);
}
