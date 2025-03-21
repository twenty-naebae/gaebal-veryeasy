package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.domain.entity.User;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import com.gaebal_easy.client.user.presentation.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // userId를 기준으로 User를 찾아 CustomUserDetails로 반환합니다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        return new CustomUserDetails(user);
    }

}
