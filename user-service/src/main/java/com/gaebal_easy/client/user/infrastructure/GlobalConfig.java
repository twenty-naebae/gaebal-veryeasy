package com.gaebal_easy.client.user.infrastructure;

import gaebal_easy.common.global.config.JpaAuditingConfig;
import gaebal_easy.common.global.config.PropertyConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        JpaAuditingConfig.class,
        PropertyConfig.class
})
public class GlobalConfig {
}
