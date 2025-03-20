package com.gaebal_easy.order.application.service;

import com.gaebal_easy.order.presentation.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubClient {
    @GetMapping("/hub-service/api/products/stock")
    String checkStock(@RequestParam List<ProductDto> products);
}
