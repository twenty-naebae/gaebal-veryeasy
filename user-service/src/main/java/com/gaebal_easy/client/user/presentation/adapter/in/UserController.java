package com.gaebal_easy.client.user.presentation.adapter.in;

import com.gaebal_easy.client.user.application.service.JoinService;
import com.gaebal_easy.client.user.application.service.UserService;
import com.gaebal_easy.client.user.presentation.dto.JoinRequest;
import com.gaebal_easy.client.user.presentation.dto.UserInfoResponse;
import com.gaebal_easy.client.user.presentation.dto.UserUpdateRequest;
import gaebal_easy.common.global.dto.ApiResponseData;
import gaebal_easy.common.global.enums.Role;
import gaebal_easy.common.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResponseData<String>> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest userUpdateRequest){
        userService.updateUser(userId, userUpdateRequest);
        return ResponseEntity.ok(ApiResponseData.success(null, "유저 정보가 수정되었습니다."));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponseData<String>> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,@PathVariable Long userId){
        userService.deleteUser(customUserDetails, userId);
        return ResponseEntity.ok(ApiResponseData.success(null, "유저 정보가 삭제되었습니다."));
    }

    // 전체 유저조회. 마스터만 가능
    @GetMapping("/users")
    public ResponseEntity<ApiResponseData<List<UserInfoResponse>>> getAllUserInfo(
            @RequestParam(required = false) Role role,
            @RequestParam(defaultValue = "asc") String sort
    ){
        return ResponseEntity.ok(ApiResponseData.success(userService.getAllUserInfo(role, sort), "유저 정보 조회에 성공하였습니다."));
    }

    // 특정 유저조회. 마스터 및 해당 유저만 가능
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponseData<UserInfoResponse>> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long userId){
        return ResponseEntity.ok(ApiResponseData.success(userService.getUserInfo(customUserDetails, userId), "유저 정보 조회에 성공하였습니다."));
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return "test: "+ customUserDetails.getUsername();
    }

    @GetMapping("/getRole/{managerId}")
    public Role getUserRole(@PathVariable("managerId") Long managerId) {
        return userService.getUserRole(managerId);
    }
}
