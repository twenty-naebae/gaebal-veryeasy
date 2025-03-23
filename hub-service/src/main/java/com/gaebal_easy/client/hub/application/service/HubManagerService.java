package com.gaebal_easy.client.hub.application.service;

import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.entity.HubManager;
import com.gaebal_easy.client.hub.domain.enums.HubLocation;
import com.gaebal_easy.client.hub.domain.repository.HubRepository;
import com.gaebal_easy.client.hub.domain.repository.HubManagerRepository;
import com.gaebal_easy.client.hub.presentation.dto.HubManagerInfoMessage;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.HubNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubManagerService {

    private final HubManagerRepository hubManagerRepository;
    private final HubRepository hubRepository;
    @Transactional
    public void createHubManager(HubManagerInfoMessage hubManagerInfoMessage) {
        Hub hub = getHub(getHubLocation(hubManagerInfoMessage.getGroup()));
        HubManager hubManager = HubManager.of(hubManagerInfoMessage.getUserId(),hubManagerInfoMessage.getName(),hub);
        hubManagerRepository.save(hubManager);
        log.info("허브 매니저 생성 : " + hubManager.getHub().getHubLocation() + ", " + hubManager.getName());
    }

    // Hubname으로 HubLocation 찾기
    private HubLocation getHubLocation(String group) {
        HubLocation hubLocation = HubLocation.findByName(group);
        if(hubLocation == null) {
            throw new HubNotFoundException(Code.HUB_NOT_FOUND);
        }
        return hubLocation;
    }

    // HubLocation으로 Hub 찾기
    private Hub getHub(HubLocation hubLocation) {
        return hubRepository.findByHubLocation(hubLocation).orElseThrow(() -> new HubNotFoundException(Code.HUB_NOT_FOUND));
    }

}
