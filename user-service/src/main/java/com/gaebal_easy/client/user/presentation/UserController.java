package com.gaebal_easy.client.user.presentation;

import com.gaebal_easy.client.user.application.service.JoinService;
import com.gaebal_easy.client.user.presentation.dto.JoinRequest;
import gaebal_easy.common.global.dto.ApiResponseData;
import gaebal_easy.common.global.enums.Role;
import gaebal_easy.common.global.security.CustomUserDetails;
import gaebal_easy.common.global.utils.RequiredRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user-service/api")
public class UserController {

    private final JoinService joinService;

    @PreAuthorize("hasRole('MASTER')")
    @GetMapping("/hello")
    public String hello(
                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ("Custom User: " + customUserDetails.getUsername()+","+customUserDetails.getRole());
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseData<String>> signup(@RequestBody JoinRequest joinRequest){
        joinService.join(joinRequest);
        return ResponseEntity.ok(ApiResponseData.success(null, "회원가입에 성공하였습니다."));
    }
}
