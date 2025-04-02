package com.gaebal_easy.delivery.infrastructure.mapper;

import com.gaebal_easy.delivery.application.dto.RequireAddressToHubServiceDto;
import com.gaebal_easy.delivery.infrastructure.dto.kafkaConsumerDto.KafkaRequireAddressToHubDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryMapper {
    public RequireAddressToHubServiceDto toDTO(KafkaRequireAddressToHubDto kafkaRequireAddressToHubDto);
}
