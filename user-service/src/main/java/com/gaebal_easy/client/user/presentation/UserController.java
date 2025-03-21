package com.gaebal_easy.client.user.presentation;

import com.gaebal_easy.client.user.application.service.JoinService;
import com.gaebal_easy.client.user.application.service.UserService;
import com.gaebal_easy.client.user.presentation.dto.JoinRequest;
import com.gaebal_easy.client.user.presentation.dto.UserUpdateRequest;
import gaebal_easy.common.global.dto.ApiResponseData;
import gaebal_easy.common.global.security.CustomUserDetails;
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

    // todo- 회원가입 예외처리. 이미 존재하는 userId일 경우등 unique 제약조건 위반시
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseData<String>> signup(@RequestBody JoinRequest joinRequest){
        joinService.join(joinRequest);
        return ResponseEntity.ok(ApiResponseData.success(null, "회원가입에 성공하였습니다."));
    }

    @PreAuthorize("hasRole('MASTER')")
    @PutMapping("/users/{userId}")
    public ResponseEntity<ApiResponseData<String>> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest userUpdateRequest){
        userService.updateUser(userId, userUpdateRequest);
        return ResponseEntity.ok(ApiResponseData.success(null, "유저 정보가 수정되었습니다."));
    }

//    @PreAuthorize("hasRole('MASTER')")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponseData<String>> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,@PathVariable Long userId){
        userService.deleteUser(customUserDetails, userId);
        return ResponseEntity.ok(ApiResponseData.success(null, "유저 정보가 삭제되었습니다."));
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return "test: "+ customUserDetails.getUsername();
    }
}
