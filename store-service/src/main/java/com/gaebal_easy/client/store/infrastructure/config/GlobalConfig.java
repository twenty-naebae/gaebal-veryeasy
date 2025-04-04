package com.gaebal_easy.client.store.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import gaebal_easy.common.global.config.JpaAuditingConfig;
import gaebal_easy.common.global.config.PropertyConfig;
import gaebal_easy.common.global.security.GlobalSecurityConfig;
import gaebal_easy.common.global.security.GlobalSecurityContextFilter;

@Configuration
@Import({
	JpaAuditingConfig.class,
	PropertyConfig.class,
	GlobalSecurityConfig.class,
	GlobalSecurityContextFilter.class
})
public class GlobalConfig {
}
