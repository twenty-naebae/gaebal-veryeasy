package com.gaebal_easy.client.user.infrastructure;

import com.gaebal_easy.client.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}
