package com.gaebal_easy.gateway.gateway.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gaebal_easy.common.global.dto.ApiResponseData;
import gaebal_easy.common.global.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AuthPermissionPreFilter extends AbstractGatewayFilterFactory<AuthPermissionPreFilter.Config> {

    private final JWTUtil jwtUtil;

    @Autowired
    public AuthPermissionPreFilter(JWTUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    // 필터에 필요한 설정이 있다면 여기에 추가
    public static class Config {
    }

    //사용자의 권한을 확인하는 필터
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            List<String> accessToken;
            // 요청 객체에서 Access 토큰을 가져온다.
            try {
                accessToken = jwtUtil.getHeaderToken(request, "access");
                // Access 토큰이 없다면 401 에러를 반환한다.
                if (accessToken == null || accessToken.isEmpty() || accessToken.get(0).trim().isEmpty()){
                    throw new RequiredArgumentException(Code.GATEWAY_REQUIRED_LOGIN, Code.GATEWAY_REQUIRED_LOGIN.getMessage());
                }

                // Access 토큰 검증
                if (!jwtUtil.isValidToken(accessToken.get(0))) {
                    if (jwtUtil.isExpired(accessToken.get(0))) {
                        throw new ExpiredTokenException(Code.GATEWAY_EXPIRED_TOKEN);
                    } else {
                        throw new InvalidTokenException(Code.GATEWAY_INVALID_TOKEN);
                    }
                }
            } catch (BaseException e) {
                return onError(exchange, e.getErrorCode());
            } catch (Exception e) {
                return onError(exchange, Code.INTERNAL_SERVER_ERROR);
            }
            // 사용자 정보를 헤더에 추가
            String userId = jwtUtil.getUserId(accessToken.get(0));
            String role = jwtUtil.getRole(accessToken.get(0));

            // 사용자 정보를 헤더에 추가
            ServerHttpRequest authRequest = request.mutate()
                    .header("Auth", "true")
                    .header("X-USER-ID", userId)
                    .header("X-USER-ROLE", role)
                    .build();

            return chain.filter(exchange.mutate().request(authRequest).build());
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, Code code) {
        exchange.getResponse().setStatusCode(code.getStatus());
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ApiResponseData<String> response = ApiResponseData.failure(code.getCode(), code.getMessage());
        byte[] bytes;
        try {
            bytes = new ObjectMapper().writeValueAsBytes(response);
        } catch (JsonProcessingException e) {
            bytes = "{}".getBytes();
        }

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
