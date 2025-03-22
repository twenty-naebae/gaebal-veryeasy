package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.domain.entity.User;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import com.gaebal_easy.client.user.presentation.dto.UserUpdateRequest;
import gaebal_easy.common.global.enums.Role;
import gaebal_easy.common.global.exception.CanNotFindUserException;
import gaebal_easy.common.global.message.HubManagerDeleteMessage;
import gaebal_easy.common.global.message.HubManagerUpdateMessage;
import gaebal_easy.common.global.security.CustomUserDetails;
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

        String newPassword = user.getPassword();
        if (userUpdateRequest.getPassword() != null) {
            newPassword = bCryptPasswordEncoder.encode(userUpdateRequest.getPassword());
        }

        String username = user.getUsername();
        if(userUpdateRequest.getUsername() != null) {
            username = userUpdateRequest.getUsername();
        }
        userRepository.update(user, userUpdateRequest.getUsername(), newPassword);
        if(user.getRole().equals(Role.HUB_MANAGER)) {
            hubManagerEventService.sendHubManagerUpdate(HubManagerUpdateMessage.of(userId, username, userUpdateRequest.getGroup()));
        }
    }

    @Transactional
    public void deleteUser(CustomUserDetails customUserDetails, Long userId) {

        // 유저 정보 삭제
        User user = userRepository.findById(userId).orElseThrow(() -> new CanNotFindUserException());
        userRepository.delete(user, customUserDetails.getUserId());
        if(user.getRole().equals(Role.HUB_MANAGER)) {
            hubManagerEventService.sendHubManagerDelete(HubManagerDeleteMessage.of(userId, customUserDetails.getUsername()));
        }

    }
}
