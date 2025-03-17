package gaebal_easy.common.global.utils;

import gaebal_easy.common.global.enums.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})  // 서비스 로직에 적용 가능
public @interface RequiredRole {
    Role role();  //Role을 속성값으로 추가
}