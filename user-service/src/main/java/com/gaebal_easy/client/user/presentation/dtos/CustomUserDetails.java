package com.gaebal_easy.client.user.presentation.dtos;

import com.gaebal_easy.client.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    //user를 반환합니다.
    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> user.getRole().name());
    }

    // password를 반환합니다.
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // username을 반환합니다.
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //id를 반환합니다.
    public Long getId() {
        return user.getId();
    }

    //UserDetails 인터페이스의 메소드들
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

