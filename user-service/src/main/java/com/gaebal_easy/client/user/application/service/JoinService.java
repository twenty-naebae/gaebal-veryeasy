package com.gaebal_easy.client.user.application.service;

import com.gaebal_easy.client.user.domain.entity.User;
import com.gaebal_easy.client.user.domain.repository.UserRepository;
import com.gaebal_easy.client.user.presentation.dto.JoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void join(JoinRequest joinRequest) {
        joinRequest.setPassword(bCryptPasswordEncoder.encode(joinRequest.getPassword()));
        User user = new User(joinRequest);
        //todo - 각 서비스에 권한에 맞는 유저 생성 이벤트 요청
        userRepository.save(user);
    }
}
