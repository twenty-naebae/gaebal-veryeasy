package com.gaebal_easy.client.user.domain.repository;

import com.gaebal_easy.client.user.domain.entity.User;

import java.util.Optional;

public interface UserRepository {

    // 유저 정보를 저장
    public User save(User user);

    // Username으로 유저 정보를 찾는다
    User findByUsername(String username);

    Optional<User> findById(Long userId);

    void update(User user, String username, String newPassword);
}
