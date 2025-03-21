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

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HubManagerEventService {

    private final KafkaTemplate<String, HubManagerInfoMessage> kafkaTemplate;
    private final KafkaTemplate<String, HubManagerUpdateMessage> updateKafkaTemplate;
    private final KafkaTemplate<String, HubManagerDeleteMessage> deleteKafkaTemplate;
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

    // hub-manager-update 이벤트 전송
    public void sendHubManagerUpdate(HubManagerUpdateMessage hubManagerUpdateMessage) {
        updateKafkaTemplate.send("hub-manager-update", hubManagerUpdateMessage);
        log.info("hub manager 업데이트 이벤트 전송 완료");
    }

    // hub-manager-delete 이벤트 전송
    public void sendHubManagerDelete(HubManagerDeleteMessage hubManagerDeleteMessage) {
        deleteKafkaTemplate.send("hub-manager-delete", hubManagerDeleteMessage);
        log.info("hub manage 삭제 이벤트 전송 완료");
    }

//    @Transactional
//    // hub-manager-update 롤백
//    public void rollbackHubManagerUpdate(HubManagerUpdateMessage hubManagerUpdateMessage) {
//        User user  = userRepository.findById(hubManagerUpdateMessage.getUserId()).orElseThrow(() -> new CanNotFindUserException());
//        userRepository
//    }
}
