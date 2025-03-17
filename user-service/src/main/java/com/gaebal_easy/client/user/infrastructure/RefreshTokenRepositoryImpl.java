package com.gaebal_easy.client.user.infrastructure;

import com.gaebal_easy.client.user.domain.entity.RefreshToken;
import com.gaebal_easy.client.user.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    //기존 리프레시 토큰을 삭제하는 메서드.
    public boolean deleteRefreshToken(String refresh){
        refreshTokenJpaRepository.deleteByRefresh(refresh);
        return true;
    }

    @Override
    public boolean isExist(String refresh) {
        return refreshTokenJpaRepository.existsByRefresh(refresh);
    }


    //해당 refreshToken이 DB에 존재하는지 확인
    public boolean isExistToken(String refresh){
        return refreshTokenJpaRepository.existsByRefresh(refresh);
    }

    //refreshToken 저장
    public boolean save(RefreshToken refreshToken){
        refreshTokenJpaRepository.save(refreshToken);
        return true;
    }

    //userId로 refreshToken을 찾는다
    public Optional<RefreshToken> getRefreshTokenByUserID(Long userId){
        return refreshTokenJpaRepository.getRefreshTokenByUserId(userId);
    }

    //refreshToken value값으로 refreshToken 객체를 찾는다.
    public Optional<RefreshToken> getRefreshTokenByValue(String value){
        return refreshTokenJpaRepository.getRefreshTokenByRefresh(value);
    }
}
