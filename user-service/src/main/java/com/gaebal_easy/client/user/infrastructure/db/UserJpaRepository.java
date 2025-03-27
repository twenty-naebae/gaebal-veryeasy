package com.gaebal_easy.client.user.infrastructure.db;

import com.gaebal_easy.client.user.domain.entity.User;
import feign.Param;
import gaebal_easy.common.global.enums.Role;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE (:role IS NULL OR u.role = :role)")
    List<User> findAllByFilter(@Param("role") Role role, Sort sort);

}
