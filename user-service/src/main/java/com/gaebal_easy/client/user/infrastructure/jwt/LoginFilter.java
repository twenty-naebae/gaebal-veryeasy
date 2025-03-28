package com.gaebal_easy.client.user.infrastructure.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.gaebal_easy.client.user.application.service.RefreshTokenService;
import com.gaebal_easy.client.user.domain.entity.RefreshToken;
import com.gaebal_easy.client.user.domain.entity.User;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import com.gaebal_easy.client.user.presentation.dto.LoginRequest;
import gaebal_easy.common.global.dto.ApiResponseData;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.RequiredArgumentException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final Long EXPIRED_MS = 86400000L*100L; //todo - 토큰 만료시간 변경 필요

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshTokenService refreshTokenService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.setFilterProcessesUrl("/user-service/api/login"); // 경로 설정
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        LoginRequest loginRequest;

        try {
            ObjectMapper objectMapper = new ObjectMapper();//json을 객체로 변환하기 위한 objectMapper
            ServletInputStream inputStream = request.getInputStream();//요청에서 데이터를 추출하기 위한 inputStream
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);//inputStream을 문자열로 변환
            loginRequest = objectMapper.readValue(messageBody, LoginRequest.class);//json을 객체로 변환
        }

        catch (UnrecognizedPropertyException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            throw new RequiredArgumentException(Code.USER_REQUIRED_ARGUMENT_EXCEPTION,"아이디, 비밀번호를 모두 입력해주세요");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());

        //반복자를 이용하여 role을 획득
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
            String role = auth.getAuthority();

        String accessToken = jwtUtil.createJwt("access", user.getId(), role, EXPIRED_MS);
        String refreshToken = jwtUtil.createJwt("refresh", user.getId(), role, EXPIRED_MS);

        addRefreshToken(user.getId(), refreshToken, EXPIRED_MS);

        response.setHeader("access", accessToken);
        response.addCookie(createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ResponseEntity<ApiResponseData<String>> responseBody = ResponseEntity.badRequest().body(ApiResponseData.failure(Code.ACCESS_DENIED_EXCEPTION.getCode(), "아이디 혹은 비밀번호를 다시 입력해주세요"));
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseBody.getBody()));
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge((EXPIRED_MS).intValue());
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshToken(Long userId, String refresh, Long expiredMs) {

        RefreshToken refreshToken = new RefreshToken(userId,refresh,expiredMs);

        refreshTokenService.saveRefreshToken(refreshToken);
    }
}