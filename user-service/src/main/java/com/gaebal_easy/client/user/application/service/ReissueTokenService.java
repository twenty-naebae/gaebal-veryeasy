package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.domain.entity.RefreshToken;
import com.gaebal_easy.client.user.domain.entity.User;
import com.gaebal_easy.client.user.domain.repository.RefreshTokenRepository;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import com.gaebal_easy.client.user.infrastructure.jwt.JWTUtil;
import gaebal_easy.common.global.exception.CanNotFindUserException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueTokenService {
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final Long EXPIRED_MS = 86400000L*100L; //todo - 토큰 만료시간 변경 필요

    @Transactional
    public void reissueToken(HttpServletRequest request, HttpServletResponse response){

        String refreshTokenValue = refreshTokenService.getRefreshTokenFromCookie(request);

        //RefreshToken 검증 후 가져오기
        RefreshToken refreshToken = refreshTokenService.getRefreshTokenAfterCheck(refreshTokenValue);

        User user = userRepository.findById(refreshToken.getUserId()).orElseThrow(()-> new CanNotFindUserException());

        //기존 토큰 DB에서 삭제
        refreshTokenRepository.deleteRefreshToken(refreshTokenValue);

        //기존 토큰  쿠키에서 삭제
        refreshTokenService.removeRefreshTokenCookie(response);

        //토큰 생성
        String newAccessToken = jwtUtil.createJwt("access", user.getId(), user.getRole().toString(), EXPIRED_MS);
        String newRefreshToken = jwtUtil.createJwt("refresh", user.getId(), user.getRole().toString(), EXPIRED_MS);

        //토큰 DB에 저장
        refreshTokenRepository.save(new RefreshToken(user.getId(),newRefreshToken,EXPIRED_MS));

        //토큰 응답 헤더, 쿠키에 저장
        response.setHeader("access", newAccessToken);
        response.addCookie(createCookie("refresh", newRefreshToken));

    }

    //쿠키 생성 메소드
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge((EXPIRED_MS).intValue());
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}
