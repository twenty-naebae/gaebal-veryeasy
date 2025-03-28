package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.domain.repository.RefreshTokenRepository;
import com.gaebal_easy.client.user.infrastructure.jwt.JWTUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteRefreshToken(refreshTokenService.getRefreshTokenAfterCheck(refreshToken));
    }
}
