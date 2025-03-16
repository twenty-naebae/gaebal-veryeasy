package com.gaebal_easy.client.user.infrastructure;

import com.gaebal_easy.client.user.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByRefresh(String refresh);

    Boolean existsByRefresh(String refresh);

    Optional<RefreshToken> getRefreshTokenByUserId(Long userId);

    Optional<RefreshToken> getRefreshTokenByRefresh(String refresh);
}
