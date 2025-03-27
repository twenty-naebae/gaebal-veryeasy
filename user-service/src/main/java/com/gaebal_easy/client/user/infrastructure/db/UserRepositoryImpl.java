package com.gaebal_easy.client.user.infrastructure.db;

import com.gaebal_easy.client.user.domain.entity.User;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import gaebal_easy.common.global.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public void update(User user, String username, String newPassword) {
        user.update(username, newPassword);
        userJpaRepository.save(user);
    }

    @Override
    public void delete(User user, String deletedBy) {
        user.delete(deletedBy);
        userJpaRepository.save(user);
    }

    @Override
    public void rollbackDelete(User user, String errorLocation) {
        user.rollbackDelete(errorLocation);
        userJpaRepository.save(user);
    }

    @Override
    public List<User> findAllByFilter(Role role, Sort sortType) {
        return userJpaRepository.findAllByFilter(role, sortType);
    }
}
