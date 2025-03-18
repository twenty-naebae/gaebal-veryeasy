package com.gaebal_easy.product.infrastructure.config;

import gaebal_easy.common.global.config.PropertyConfig;
import gaebal_easy.common.global.entity.BaseTimeEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        PropertyConfig.class,
})
public class GlobalConfig {
}
