package com.gaebal_easy.client.user.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.user.application.service.LogoutService;
import com.gaebal_easy.client.user.application.service.RefreshTokenService;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import com.gaebal_easy.client.user.infrastructure.jwt.CustomLogoutFilter;
import com.gaebal_easy.client.user.infrastructure.jwt.JWTUtil;
import com.gaebal_easy.client.user.infrastructure.jwt.LoginFilter;
import gaebal_easy.common.global.enums.Role;
import gaebal_easy.common.global.security.GlobalSecurityContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
public class UserSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RefreshTokenService refreshTokenService;
    private final GlobalSecurityContextFilter globalSecurityContextFilter;
    private final LogoutService logoutService;

    public UserSecurityConfig(AuthenticationConfiguration authenticationConfiguration,
                              JWTUtil jwtUtil,
                              RefreshTokenService refreshTokenService,
                              UserRepository userRepository,
                              ObjectMapper objectMapper,
                              GlobalSecurityContextFilter globalSecurityContextFilter,
                              LogoutService logoutService
    ) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.globalSecurityContextFilter = globalSecurityContextFilter;
        this.logoutService = logoutService;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                .securityMatcher("/user-service/api/login")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user-service/api/signup", "/user-service/api/login", "/user-service/api/getRole/**").permitAll()
                        .requestMatchers("/user-service/api/users").hasRole("MASTER")
                        .requestMatchers("/user-service/api/users/**").authenticated()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers( // swagger관련 url
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated())
                .csrf((auth) -> auth.disable())
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .logout((auth) -> auth.disable())
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil,
                        refreshTokenService, userRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(globalSecurityContextFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //로그아웃 필터 추가
        http
                .addFilterBefore(new CustomLogoutFilter(objectMapper, refreshTokenService,logoutService ), LogoutFilter.class);


        return http.build();
    }
}
