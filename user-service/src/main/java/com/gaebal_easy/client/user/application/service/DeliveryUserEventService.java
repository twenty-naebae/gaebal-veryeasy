package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.application.dto.HubManagerInfoMessage;
import com.gaebal_easy.client.user.domain.entity.User;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import gaebal_easy.common.global.exception.CanNotFindUserException;
import gaebal_easy.common.global.message.DeliveryUserDeleteMessage;
import gaebal_easy.common.global.message.DeliveryUserInfoMessage;
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
public class DeliveryUserEventService {

    private final KafkaTemplate<String, DeliveryUserInfoMessage> kafkaTemplate;
    private final KafkaTemplate<String, DeliveryUserDeleteMessage> deleteKafkaTemplate;
    private final UserRepository userRepository;

    public void sendDeliveryUserInfo(DeliveryUserInfoMessage deliveryUserInfoMessage) {
        kafkaTemplate.send("delivery-user-create", deliveryUserInfoMessage);
        log.info("전송 완료");
    }

    @Transactional
    public void rollbackDeliveryUserInfo(DeliveryUserInfoMessage deliveryUserInfoMessage) {
        User user  = userRepository.findById(deliveryUserInfoMessage.getUserId()).orElseThrow(() -> new CanNotFindUserException());
        user.delete(deliveryUserInfoMessage.getErrorLocation());
        userRepository.save(user);
        log.info("유저 생성 롤백 완료");
    }

    public void sendDeliveryUserDelete(DeliveryUserDeleteMessage deliveryUserDeleteMessage) {
        deleteKafkaTemplate.send("delivery-user-delete", deliveryUserDeleteMessage);
        log.info("hub manage 삭제 이벤트 전송 완료");
    }

    @Transactional
    public void rollbackDeliveryUserDelete(DeliveryUserDeleteMessage deliveryUserDeleteMessage) {
        User user = userRepository.findById(deliveryUserDeleteMessage.getUserId()).orElseThrow(() -> new CanNotFindUserException());
        userRepository.rollbackDelete(user, deliveryUserDeleteMessage.getErrorLocation());
    }
}
