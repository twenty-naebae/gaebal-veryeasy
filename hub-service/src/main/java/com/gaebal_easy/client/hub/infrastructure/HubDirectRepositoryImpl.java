package com.gaebal_easy.client.hub.infrastructure;

import com.gaebal_easy.client.hub.domain.entity.HubDirectMovementInfo;
import com.gaebal_easy.client.hub.domain.repository.HubDirectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HubDirectRepositoryImpl implements HubDirectRepository {
    private final HubDirectJpaRepository hubDirectJpaRepository;

    public List<HubDirectMovementInfo> findAll() {return hubDirectJpaRepository.findAll();}

    public void save(HubDirectMovementInfo hubDirectMovementInfo) {hubDirectJpaRepository.save(hubDirectMovementInfo);}
}
