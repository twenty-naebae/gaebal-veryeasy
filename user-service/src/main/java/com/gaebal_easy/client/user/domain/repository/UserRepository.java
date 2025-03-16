package com.gaebal_easy.client.user.domain.repository;

import com.gaebal_easy.client.user.domain.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    public User save(User user);
}
