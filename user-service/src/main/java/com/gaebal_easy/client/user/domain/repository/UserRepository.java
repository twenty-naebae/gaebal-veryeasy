package com.gaebal_easy.client.user.domain.repository;

import com.gaebal_easy.client.user.domain.entity.User;
import org.springframework.stereotype.Repository;

public interface UserRepository {

    // 유저 정보를 저장
    public User save(User user);

    // Username으로 유저 정보를 찾는다
    User findByUsername(String username);

}
