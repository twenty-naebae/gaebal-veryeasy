package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.domain.entity.User;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import com.gaebal_easy.client.user.presentation.dto.CustomUserDetails;
import com.gaebal_easy.client.user.presentation.dto.UserUpdateRequest;
import gaebal_easy.common.global.exception.CanNotFindUserException;
import gaebal_easy.common.global.message.HubManagerUpdateMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final HubManagerEventService hubManagerEventService;

    @Transactional
    public void updateUser(Long userId, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CanNotFindUserException());
        String newPassword = bCryptPasswordEncoder.encode(userUpdateRequest.getPassword());
        userRepository.update(user, userUpdateRequest.getUsername(), newPassword);
        hubManagerEventService.sendHubManagerUpdate(HubManagerUpdateMessage.of(userId, userUpdateRequest.getName(), userUpdateRequest.getGroup()));
    }

}
