package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.domain.entity.User;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import com.gaebal_easy.client.user.presentation.adapter.out.DeliveryUserEventConsumer;
import com.gaebal_easy.client.user.presentation.adapter.out.HubManagerEventConsumer;
import com.gaebal_easy.client.user.presentation.dto.UserInfoResponse;
import com.gaebal_easy.client.user.presentation.dto.UserUpdateRequest;
import gaebal_easy.common.global.enums.Role;
import gaebal_easy.common.global.exception.CanNotAccessInfoException;
import gaebal_easy.common.global.exception.CanNotFindUserException;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.message.DeliveryUserDeleteMessage;
import gaebal_easy.common.global.message.HubManagerDeleteMessage;
import gaebal_easy.common.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final HubManagerEventConsumer hubManagerEventConsumer;
    private final DeliveryUserEventConsumer deliveryUserEventConsumer;

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

        userRepository.update(user, username, newPassword);
    }

    @Transactional
    public void deleteUser(CustomUserDetails customUserDetails, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CanNotFindUserException());
        userRepository.delete(user, customUserDetails.getUserId());
        if(user.getRole().equals(Role.HUB_MANAGER)) {
            hubManagerEventConsumer.sendHubManagerDelete(HubManagerDeleteMessage.of(userId, customUserDetails.getUserId()));
        }
        else if(user.getRole().equals(Role.HUB_DELIVERY_USER)|| user.getRole().equals(Role.STORE_DELIVERY_USER)){
            deliveryUserEventConsumer.sendDeliveryUserDelete(DeliveryUserDeleteMessage.of(userId, customUserDetails.getUserId()));
        }
    }

    @Transactional(readOnly = true)
    public List<UserInfoResponse> getAllUserInfo(Role role, String sort) {
        Sort sortType;
        if(sort.equals("asc")) {
            sortType = Sort.by(Sort.Order.asc("id"));
        } else {
            sortType = Sort.by(Sort.Order.desc("id"));
        }
        List<User> users = userRepository.findAllByFilter(role, sortType);
        List<UserInfoResponse> userInfoResponses = new ArrayList<>();
        for(User user : users) {
            userInfoResponses.add(UserInfoResponse.of(user.getId(),user.getUsername(), user.getRole().toString()));
        }
        return userInfoResponses;
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(CustomUserDetails customUserDetails, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CanNotFindUserException());
        // 마스터가 아닌 경우 해당 유저의 정보만 조회 가능
        if(!customUserDetails.getUserId().equals(userId.toString()) && !customUserDetails.getRole().equals(Role.MASTER.getRoleName())) {
            throw new CanNotAccessInfoException(Code.CAN_NOT_ACCESS_USER_INFO);
        }
        return UserInfoResponse.of(user.getId(), user.getUsername(), user.getRole().toString());
    }

    @Transactional(readOnly = true)
    public Role getUserRole(Long managerId) {
        User user = userRepository.findById(managerId)
            .orElseThrow(() -> new CanNotFindUserException());

        return user.getRole();
    }
}
