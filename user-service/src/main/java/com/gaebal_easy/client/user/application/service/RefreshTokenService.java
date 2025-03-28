package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.domain.entity.RefreshToken;
import com.gaebal_easy.client.user.domain.repository.RefreshTokenRepository;
import com.gaebal_easy.client.user.infrastructure.jwt.JWTUtil;
import gaebal_easy.common.global.exception.CanNotFindTokenException;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.ExpiredTokenException;
import gaebal_easy.common.global.exception.RequiredArgumentException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public void removeRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new RequiredArgumentException(Code.USER_REQUIRED_ARGUMENT_EXCEPTION,"쿠키에 refresh토큰이 존재하지 않습니다.");
    }

    public RefreshToken getRefreshTokenAfterCheck(String refreshToken){
        checkExpired(refreshToken);
        checkRefreshTokenCategory(refreshToken);
        return getRefreshTokenInDB(refreshToken);
    }

    private void checkExpired(String refreshToken){
        if (jwtUtil.isExpired(refreshToken)) {
            throw new ExpiredTokenException(Code.USER_EXPIRED_TOKEN);
        }
    }

    private void checkRefreshTokenCategory(String refreshToken){
        String category = jwtUtil.getCategory(refreshToken);
        if (!"refresh".equals(category)) {
            throw new RequiredArgumentException(Code.USER_REQUIRED_ARGUMENT_EXCEPTION,"리프레시 토큰이 아닙니다.");
        }
    }

    public RefreshToken getRefreshTokenInDB(String refreshToken){
        return refreshTokenRepository.getRefreshTokenByValue(refreshToken)
                .orElseThrow(()->new CanNotFindTokenException(Code.USER_CAN_NOT_FIND_TOKEN));

    }
}

