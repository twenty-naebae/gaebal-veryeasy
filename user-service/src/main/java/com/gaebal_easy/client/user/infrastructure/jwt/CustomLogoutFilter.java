package com.gaebal_easy.client.user.infrastructure.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaebal_easy.client.user.application.service.LogoutService;
import com.gaebal_easy.client.user.application.service.RefreshTokenService;
import gaebal_easy.common.global.dto.ApiResponseData;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.ExpiredTokenException;
import gaebal_easy.common.global.exception.InvalidTokenException;
import gaebal_easy.common.global.exception.RequiredArgumentException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private static final String LOGOUT_URL = "/user-service/api/logout"; // 로그아웃 URL 상수
    private final ObjectMapper objectMapper; // JSON 직렬화를 위한 ObjectMapper
    private final RefreshTokenService refreshTokenService;
    private final LogoutService logoutService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (!LOGOUT_URL.equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }


        try {
            String refresh = refreshTokenService.getRefreshTokenFromCookie(request);
            // 🔥 Service에서 모든 검증 & 삭제 처리
            logoutService.logout(refresh);

            // 로그아웃 성공 시 쿠키 삭제
            refreshTokenService.removeRefreshTokenCookie(response);
            sendSuccessResponse(response, HttpServletResponse.SC_OK, "성공적으로 로그아웃 되었습니다.");
        } catch (RequiredArgumentException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage() + " : " +"쿠키에 refresh토큰이 존재하지 않습니다.", Code.USER_REQUIRED_ARGUMENT_EXCEPTION);
        } catch (ExpiredTokenException e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, Code.USER_EXPIRED_TOKEN.getMessage(), Code.USER_EXPIRED_TOKEN);
        } catch (InvalidTokenException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, Code.USER_INVALID_TOKEN.getMessage(), Code.USER_INVALID_TOKEN);
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류", Code.INTERNAL_SERVER_ERROR);
        }
    }

    // 성공 응답을 생성하는 메서드
    private void sendSuccessResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponseData.success(null,message)));
    }

    // 에러 응답을 생성하는 메서드
    private void sendErrorResponse(HttpServletResponse response, int status, String message, Code code) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        response.getWriter().write(objectMapper.writeValueAsString(ApiResponseData.of(code.getCode(), message, null)));
    }

}
