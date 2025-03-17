package gaebal_easy.common.global.utils;

import gaebal_easy.common.global.enums.Role;
import gaebal_easy.common.global.exception.AccessDeniedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequiredRoleAspect {

    @Before("@annotation(checkAuth) && args(.., role)")
    public void beforeMethod(JoinPoint joinPoint, RequiredRole checkAuth, String role) {
        Role requiredRole = checkAuth.role();  // 어노테이션에서 지정한 Role 가져오기

        if (!role.equals(requiredRole.toString())) {
            throw new AccessDeniedException("권한이 없습니다. 필요한 권한 : " + requiredRole.getRoleName());
        }
    }
}