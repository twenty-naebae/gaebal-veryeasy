package com.gaebal_easy.client.store.presentation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import gaebal_easy.common.global.enums.Role;

@FeignClient(name = "user-service")
public interface UserServiceClient {

	@GetMapping("user-service/api/{managerId}/role")
	Role getUserRole(@PathVariable("managerId") Long managerId);
}
