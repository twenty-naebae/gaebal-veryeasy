package com.gaebal_easy.client.hub.application.service.config;

import com.gaebal_easy.client.hub.application.dto.kafkaConsumerDto.KafkaOrderStoreInfoDto;
import com.gaebal_easy.client.hub.application.dto.kafkaProducerDto.KafkaOrderFailDto;
import com.gaebal_easy.client.hub.application.dto.kafkaProducerDto.KafkaRequireAddressToHubDto;
import com.gaebal_easy.client.hub.application.dto.kafkaProducerDto.KafkaStoreCreateAssignDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
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

    private <T> ProducerFactory<String, T> createProducerFactory(Class<T> targetType) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }
    @Bean
    public ProducerFactory<String, KafkaStoreCreateAssignDto> storeCreateAssignProducerFactory() {
        return createProducerFactory(KafkaStoreCreateAssignDto.class);
    }

    @Bean
    public KafkaTemplate<String, KafkaStoreCreateAssignDto> storeCreateAssignKafkaTemplate() {
        return new KafkaTemplate<>(storeCreateAssignProducerFactory());
    }

    @Bean
    public ProducerFactory<String, KafkaOrderFailDto> orderFailProducerFactory() {
        return createProducerFactory(KafkaOrderFailDto.class);
    }

    @Bean
    public KafkaTemplate<String, KafkaOrderFailDto> orderFailKafkaTemplate() {
        return new KafkaTemplate<>(orderFailProducerFactory());
    }

    @Bean
    public ProducerFactory<String, KafkaRequireAddressToHubDto> requireAddressToHubProducerFactory() {
        return createProducerFactory(KafkaRequireAddressToHubDto.class);
    }

    @Bean
    public KafkaTemplate<String, KafkaRequireAddressToHubDto> requireAddressToHubKafkaTemplate() {
        return new KafkaTemplate<>(requireAddressToHubProducerFactory());
    }
}
