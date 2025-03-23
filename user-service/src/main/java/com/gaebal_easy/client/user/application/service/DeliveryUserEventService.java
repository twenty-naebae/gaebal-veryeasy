package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.application.dto.HubManagerInfoMessage;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import gaebal_easy.common.global.message.HubManagerDeleteMessage;
import gaebal_easy.common.global.message.HubManagerUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryUserEventService {

    private final KafkaTemplate<String, HubManagerInfoMessage> kafkaTemplate;
    private final KafkaTemplate<String, HubManagerUpdateMessage> updateKafkaTemplate;
    private final KafkaTemplate<String, HubManagerDeleteMessage> deleteKafkaTemplate;
    private final UserRepository userRepository;

}
