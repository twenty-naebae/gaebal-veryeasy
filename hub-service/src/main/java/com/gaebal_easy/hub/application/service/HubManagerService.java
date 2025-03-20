package com.gaebal_easy.hub.application.service;

import com.gaebal_easy.hub.domain.entity.HubManager;
import com.gaebal_easy.hub.domain.entity.HubTemp;
import com.gaebal_easy.hub.domain.repository.HubManagerRepository;
import com.gaebal_easy.hub.domain.repository.HubTempRepository;
import com.gaebal_easy.hub.presentation.dto.HubManagerInfoMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubManagerService {

    private final HubManagerRepository hubManagerRepository;
    private final HubTempRepository hubTempRepository;
    @Transactional
    public void createHubManager(HubManagerInfoMessage hubManagerInfoMessage) {
        HubTemp hub = hubTempRepository.findByHubName(hubManagerInfoMessage.getGroup()); //todo - hub 발견하지 못할경우 예외처리
        HubManager hubManager = HubManager.of(hub, hubManagerInfoMessage.getName());
        hubManagerRepository.save(hubManager);
        log.info("허브 매니저 생성 : " + hubManager.getHub().getHubName() + ", " + hubManager.getManagerName());
    }
}
