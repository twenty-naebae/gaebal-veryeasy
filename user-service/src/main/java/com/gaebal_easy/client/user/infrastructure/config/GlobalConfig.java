package com.gaebal_easy.client.user.infrastructure.config;

import gaebal_easy.common.global.config.JpaAuditingConfig;
import gaebal_easy.common.global.config.PropertyConfig;
import gaebal_easy.common.global.security.GlobalSecurityConfig;
import gaebal_easy.common.global.security.GlobalSecurityContextFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        JpaAuditingConfig.class,
        PropertyConfig.class,
        GlobalSecurityContextFilter.class
})
public class GlobalConfig {
}
