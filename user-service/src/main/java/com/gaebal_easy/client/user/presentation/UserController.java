package com.gaebal_easy.client.user.presentation;

import com.gaebal_easy.client.user.application.service.JoinService;
import com.gaebal_easy.client.user.presentation.dtos.JoinRequest;
import gaebal_easy.common.global.dto.ApiResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user-service/api")
public class UserController {

    private final JoinService joinService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseData<String>> signup(@RequestBody JoinRequest joinRequest){
        joinService.join(joinRequest);
        return ResponseEntity.ok(ApiResponseData.success(null, "회원가입에 성공하였습니다."));
    }
}
