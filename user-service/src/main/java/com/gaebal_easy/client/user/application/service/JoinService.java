package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.application.dto.HubManagerInfoMessage;
import com.gaebal_easy.client.user.domain.entity.User;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import com.gaebal_easy.client.user.presentation.dto.JoinRequest;
import gaebal_easy.common.global.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ProducerService producerService;

    //todo - validation 체크 해줘야함. unique검사등
    @Transactional
    public void join(JoinRequest joinRequest) {
        joinRequest.setPassword(bCryptPasswordEncoder.encode(joinRequest.getPassword()));
        User user = new User(joinRequest);
        userRepository.save(user);
        //todo - 각 서비스에 권한에 맞는 유저 생성 이벤트 요청
        if(user.getRole().equals(Role.HUB_MANAGER)){
            producerService.sendHubManagerInfo(HubManagerInfoMessage.of(user.getId(), joinRequest.getName(), joinRequest.getGroup()));
        }
        else if(user.getRole().equals(Role.HUB_DELIVERY_USER)|| user.getRole().equals(Role.STORE_DELIVERY_USER)){
            //todo - sendDeliverUserInfo 처리
        }
    }
}
