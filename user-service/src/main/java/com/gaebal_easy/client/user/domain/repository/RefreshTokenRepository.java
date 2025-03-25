package com.gaebal_easy.client.user.domain.repository;

import com.gaebal_easy.client.user.domain.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    //기존 리프레시 토큰을 삭제하는 메서드.
    public boolean deleteRefreshToken(String refresh);

    public boolean deleteRefreshToken(RefreshToken refresh);

    //해당 refreshToken이 DB에 존재하는지 확인
    public boolean isExist(String refresh);

    //refreshToken 저장
    public boolean save(RefreshToken refreshToken);

    //userId로 refreshToken을 찾는다
    public Optional<RefreshToken> getRefreshTokenByUserID(Long userId);

    Optional<RefreshToken> getRefreshTokenByValue(String refreshToken);
}
