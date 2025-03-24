package com.gaebal_easy.client.store.presentation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import gaebal_easy.common.global.enums.Role;

@FeignClient(name = "user-service")
public interface UserServiceClient {

	@GetMapping("user-service/api/getRole/{managerId}")
	Role getUserRole(@RequestHeader("access") String access,@PathVariable("managerId") Long managerId);
}
