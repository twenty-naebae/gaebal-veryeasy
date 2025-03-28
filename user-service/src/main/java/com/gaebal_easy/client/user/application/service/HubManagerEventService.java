package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.application.dto.HubManagerInfoMessage;
import com.gaebal_easy.client.user.domain.entity.User;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import gaebal_easy.common.global.exception.CanNotFindUserException;
import gaebal_easy.common.global.message.HubManagerDeleteMessage;
import gaebal_easy.common.global.message.HubManagerUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubManagerEventService {

    private final UserRepository userRepository;

    @Transactional
    public void rollbackHubManagerInfo(HubManagerInfoMessage hubManagerInfoMessage) {
        User user  = userRepository.findById(hubManagerInfoMessage.getUserId()).orElseThrow(() -> new CanNotFindUserException());
        user.delete(hubManagerInfoMessage.getErrorLocation());
        userRepository.save(user);
        log.info("유저 롤백 완료");
    }

    // hub-manager-delete 롤백
    @Transactional
    public void rollbackHubMangerDelete(HubManagerDeleteMessage hubManagerDeleteMessage) {
        User user = userRepository.findById(hubManagerDeleteMessage.getUserId()).orElseThrow(() -> new CanNotFindUserException());
        userRepository.rollbackDelete(user, hubManagerDeleteMessage.getErrorLocation());
    }

}
