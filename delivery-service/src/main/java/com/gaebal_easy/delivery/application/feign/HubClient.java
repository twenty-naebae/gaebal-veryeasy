package com.gaebal_easy.delivery.application.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hub-service", url = "http://hub-backend:19095")
public interface HubClient {

    @GetMapping("/hub-service/api/route")
    HubRouteDto getRoute(@RequestParam("depart") String depart,
                         @RequestParam("arrive") String arrive);
    @GetMapping("/hub-service/api/direct")
    HubDirectDto getDirectHub(@RequestParam("depart") String depart,
                         @RequestParam("arrive") String arrive);

    @GetMapping("/hub-service/api/coordinate")
    HubLocationDto getCoordinate(@RequestParam("hubName") String hubName);
}