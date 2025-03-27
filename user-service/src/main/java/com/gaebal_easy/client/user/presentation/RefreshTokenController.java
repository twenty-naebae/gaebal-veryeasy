package com.gaebal_easy.client.user.presentation;

import com.gaebal_easy.client.user.application.service.ReissueTokenService;
import gaebal_easy.common.global.dto.ApiResponseData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-service/api/refresh-token")
public class RefreshTokenController {

    private final ReissueTokenService reissueTokenService;

    @PostMapping("reissue")
    public ResponseEntity<ApiResponseData<String>> reissueToken(
            HttpServletRequest request,
            HttpServletResponse response)
    {
        reissueTokenService.reissueToken(request,response);
        return ResponseEntity.ok(ApiResponseData.success(null,"정상적으로 재발급 되었습니다."));
    }
}
