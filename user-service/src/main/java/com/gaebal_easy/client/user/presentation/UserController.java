package com.gaebal_easy.client.user.presentation;

import com.gaebal_easy.client.user.application.service.JoinService;
import com.gaebal_easy.client.user.application.service.HubManagerEventService;
import com.gaebal_easy.client.user.application.service.UserService;
import com.gaebal_easy.client.user.presentation.dto.CustomUserDetails;
import com.gaebal_easy.client.user.presentation.dto.JoinRequest;
import com.gaebal_easy.client.user.presentation.dto.UserUpdateRequest;
import gaebal_easy.common.global.dto.ApiResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user-service/api")
public class UserController {

    private final JoinService joinService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseData<String>> signup(@RequestBody JoinRequest joinRequest){
        joinService.join(joinRequest);
        return ResponseEntity.ok(ApiResponseData.success(null, "회원가입에 성공하였습니다."));
    }

    @PreAuthorize("hasRole('MASTER')")
    @PutMapping("/users/{userId}")
    public ResponseEntity<ApiResponseData<String>> updateUser(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long userId, @RequestBody UserUpdateRequest userUpdateRequest){
        userService.updateUser(userId, userUpdateRequest);
        return ResponseEntity.ok(ApiResponseData.success(null, "유저 정보가 수정되었습니다."));
    }
}
