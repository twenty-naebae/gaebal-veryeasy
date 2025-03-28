package com.gaebal_easy.client.hub.application.service;

import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.entity.HubManager;
import com.gaebal_easy.client.hub.domain.enums.HubLocation;
import com.gaebal_easy.client.hub.domain.repository.HubRepository;
import com.gaebal_easy.client.hub.domain.repository.HubManagerRepository;
import com.gaebal_easy.client.hub.presentation.dto.HubManagerInfoMessage;
import com.gaebal_easy.client.hub.presentation.dto.HubManagerInfoResposne;
import com.gaebal_easy.client.hub.presentation.dto.HubManagerUpdateRequest;
import gaebal_easy.common.global.exception.CanNotAccessInfoException;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.HubManagerNotFoundException;
import gaebal_easy.common.global.exception.HubNotFoundException;
import gaebal_easy.common.global.message.HubManagerDeleteMessage;
import gaebal_easy.common.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    public void deleteHubManager(HubManagerDeleteMessage hubManagerDeleteMessage) {
        // 허브 매니저 찾기
        HubManager hubManager = hubManagerRepository.findByUserId(hubManagerDeleteMessage.getUserId()).orElseThrow(() -> new HubManagerNotFoundException(Code.HUB_CAN_NOT_FIND_HUBMANAGER));
        hubManagerRepository.delete(hubManager, hubManagerDeleteMessage.getDeletedBy());
    }

    @Transactional(readOnly = true)
    public List<HubManagerInfoResposne> getAllHubManagerInfo(Long hubId,String sort) {
        Sort sortType;
        if(sort.equals("asc")) {
            sortType = Sort.by(Sort.Order.asc("createdAt"));
        } else {
            sortType = Sort.by(Sort.Order.desc("createdAt"));
        }

        List<HubManager> hubManagers = hubManagerRepository.findAllByFilter(hubId, sortType);
        List<HubManagerInfoResposne> userInfoResponses = new ArrayList<>();
        for(HubManager hubManager : hubManagers) {
            userInfoResponses.add(HubManagerInfoResposne.of(hubManager.getId(),hubManager.getUserId(),hubManager.getName(),hubManager.getHub().getId()));
        }
        return userInfoResponses;
    }

    @Transactional(readOnly = true)
    public HubManagerInfoResposne getHubManagerInfo(CustomUserDetails customUserDetails, Long userId) {

        HubManager hubManager = hubManagerRepository.findByUserId(userId)
                .orElseThrow(() -> new HubManagerNotFoundException(Code.HUB_CAN_NOT_FIND_HUBMANAGER));

        if(!customUserDetails.getRole().equals("MASTER") && !customUserDetails.getUserId().equals(hubManager.getUserId().toString())) {
            throw new CanNotAccessInfoException(Code.HUB_CAN_NOT_ACCESS_INFO);
        }

        return HubManagerInfoResposne.of(
                hubManager.getId(),
                hubManager.getUserId(),
                hubManager.getName(),
                hubManager.getHub().getId()
        );

    }

    // 허브 매니저 정보 수정(허브 매니저 이름, 허브 변경)
    @Transactional
    public void updateHubManager(HubManagerUpdateRequest request) {
        HubManager hubManager = hubManagerRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new HubManagerNotFoundException(Code.HUB_CAN_NOT_FIND_HUBMANAGER));

        Hub newHub = hubRepository.findById(request.getHubId())
                .orElseThrow(() -> new HubNotFoundException(Code.HUB_NOT_FOUND));

        hubManagerRepository.update(hubManager, request.getName(), newHub);
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
