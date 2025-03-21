package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.application.dto.HubManagerInfoMessage;
import com.gaebal_easy.client.user.domain.entity.User;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import gaebal_easy.common.global.exception.CanNotFindUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubManagerEventService {

    private final KafkaTemplate<String, HubManagerInfoMessage> kafkaTemplate;
    private final UserRepository userRepository;

    // hub-manager-create 이벤트 전송
    public void sendHubManagerInfo(HubManagerInfoMessage hubManagerInfoMessage) {
        kafkaTemplate.send("hub-manager-create", hubManagerInfoMessage);
        log.info("전송 완료");
    }

    @Transactional
    // hub-manager-create 롤백
    public void rollbackHubManagerInfo(HubManagerInfoMessage hubManagerInfoMessage) {
        User user  = userRepository.findById(hubManagerInfoMessage.getUserId()).orElseThrow(() -> new CanNotFindUserException());
        user.delete(hubManagerInfoMessage.getErrorLocation());
        userRepository.save(user);
        log.info("유저 롤백 완료");
    }
}
