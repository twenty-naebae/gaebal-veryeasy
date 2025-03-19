package com.gaebal_easy.gateway.gateway.infrastructure;

import org.springframework.boot.actuate.autoconfigure.security.reactive.ReactiveManagementWebSecurityAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import gaebal_easy.common.global.config.PropertyConfig;

@Configuration
@Import({
        PropertyConfig.class
})
@ImportAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class,
        ReactiveSecurityAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class,
        ReactiveManagementWebSecurityAutoConfiguration.class
})
public class GlobalConfig {
}