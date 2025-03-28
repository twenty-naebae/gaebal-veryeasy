package com.gaebal_easy.client.store.infrastructure.adapter.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import gaebal_easy.common.global.enums.Role;

@FeignClient(name = "user-service")
public interface UserServiceWebClient {

	@GetMapping("user-service/api/getRole/{managerId}")
	Role getUserRole(@PathVariable("managerId") Long managerId);
}
