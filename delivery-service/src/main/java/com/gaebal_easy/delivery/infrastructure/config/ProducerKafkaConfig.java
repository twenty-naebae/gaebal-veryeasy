package com.gaebal_easy.delivery.infrastructure.config;

import com.gaebal_easy.delivery.presentation.dto.kafkaProducerDto.SlackMessageInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@EnableKafka
public class ProducerKafkaConfig {
    @Value("${spring.kafka.url}")
    private String kafkaServerUrl;

    private <T> ProducerFactory<String, T> createProducerFactory(Class<T> targetType) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }
    @Bean
    public ProducerFactory<String, SlackMessageInfoDTO> slackMessageProducerFactory() {
        return createProducerFactory(SlackMessageInfoDTO.class);
    }

    @Bean
    public KafkaTemplate<String, SlackMessageInfoDTO> slackMessageKafkaTemplate() {
        return new KafkaTemplate<>(slackMessageProducerFactory());
    }
}
