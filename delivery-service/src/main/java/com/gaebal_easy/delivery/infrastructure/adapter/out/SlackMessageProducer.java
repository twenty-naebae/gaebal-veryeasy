package com.gaebal_easy.delivery.infrastructure.adapter.out;


import com.gaebal_easy.delivery.presentation.dto.kafkaProducerDto.SlackMessageInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackMessageProducer {
    private static final String TOPIC = "delivery-assigned-topic";

    private final KafkaTemplate<String, SlackMessageInfoDTO> slackMessageKafkaTemplate;

    public void slackMessageEvent(SlackMessageInfoDTO slackMessageInfoDTO) {
        log.info("slack Message Event 발행 "+"\uD83C\uDFAF 목표 도달!");
        slackMessageKafkaTemplate.send(TOPIC, slackMessageInfoDTO);
    }
}
