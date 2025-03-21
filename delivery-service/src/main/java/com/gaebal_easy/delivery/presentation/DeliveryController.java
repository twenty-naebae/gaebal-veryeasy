package com.gaebal_easy.delivery.presentation;


import com.gaebal_easy.delivery.application.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery-service/api")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @GetMapping("/send")
    public String sendMessage() {
        deliveryService.assignDeliveryManager();
        return "Message sent to Kafka topic";
    }

}
