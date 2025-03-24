package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.application.dto.HubManagerInfoMessage;
import com.gaebal_easy.client.user.domain.entity.User;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import com.gaebal_easy.client.user.presentation.dto.JoinRequest;
import gaebal_easy.common.global.enums.Role;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.UserUniqueException;
import gaebal_easy.common.global.message.DeliveryUserInfoMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final HubManagerEventService hubManagerEventService;
    private final DeliveryUserEventService deliveryUserEventService;

    @Transactional
    public void join(JoinRequest joinRequest) {
        // username 중복 체크
        checkExistsUsername(joinRequest.getUsername());
        joinRequest.setPassword(bCryptPasswordEncoder.encode(joinRequest.getPassword()));
        User user = new User(joinRequest);
        userRepository.save(user);
        if(user.getRole().equals(Role.HUB_MANAGER)){
            hubManagerEventService.sendHubManagerInfo(HubManagerInfoMessage.of(user.getId(), joinRequest.getName(), joinRequest.getGroup()));
        }
        else if(user.getRole().equals(Role.HUB_DELIVERY_USER)|| user.getRole().equals(Role.STORE_DELIVERY_USER)){
            deliveryUserEventService.sendDeliveryUserInfo(DeliveryUserInfoMessage.of(user.getId(), joinRequest.getName(), joinRequest.getGroup(), joinRequest.getSlackId()));
        }
    }

    private void checkExistsUsername(String username) {
        if(userRepository.findByUsername(username)!=null){
            throw new UserUniqueException(Code.USER_ALREADY_USERNAME_EXCEPTION);
        }
    }
}
