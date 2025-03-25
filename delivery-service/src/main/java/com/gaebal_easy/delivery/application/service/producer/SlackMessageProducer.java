package com.gaebal_easy.delivery.application.service.producer;


import com.gaebal_easy.delivery.application.dto.kafkaProducerDto.SlackMessageInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SlackMessageProducer {
    private static final String TOPIC = "delivery-assigned-topic";

    private final KafkaTemplate<String, SlackMessageInfoDTO> slackMessageKafkaTemplate;

    public void slackMessageEvent(SlackMessageInfoDTO slackMessageInfoDTO) {
        slackMessageKafkaTemplate.send(TOPIC, slackMessageInfoDTO);
    }
}
