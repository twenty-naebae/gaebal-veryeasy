package com.gaebal_easy.client.hub.presentation.adapter.in;

import com.gaebal_easy.client.hub.application.service.HubManagerService;
import com.gaebal_easy.client.hub.presentation.dto.HubManagerInfoResposne;
import com.gaebal_easy.client.hub.presentation.dto.HubManagerUpdateRequest;
import gaebal_easy.common.global.dto.ApiResponseData;
import gaebal_easy.common.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hub-service/api")
public class HubManagerController {

    private final HubManagerService hubManagerService;
    // 전체 허브 관리자조회. 마스터만 가능
    @GetMapping("/hub-managers")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<ApiResponseData<List<HubManagerInfoResposne>>> getAllHubManagerInfo(
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) Long hubId
    ){
        return ResponseEntity.ok(ApiResponseData.success(hubManagerService.getAllHubManagerInfo(hubId,sort), "허브 관리자 정보 조회에 성공하였습니다."));
    }

    // 특정 허브 관리자 상세 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponseData<HubManagerInfoResposne>> getHubManagerInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long userId) {
        HubManagerInfoResposne response = hubManagerService.getHubManagerInfo(customUserDetails,userId);
        return ResponseEntity.ok(ApiResponseData.success(response, "허브 관리자 상세 정보 조회 성공"));
    }

    // 허브 관리자 정보 수정
    @PatchMapping("/users")
    @PreAuthorize("hasRole('MASTER')")
    public ResponseEntity<ApiResponseData<String>> updateHubManager(@RequestBody HubManagerUpdateRequest request) {
        hubManagerService.updateHubManager(request);
        return ResponseEntity.ok(ApiResponseData.success(null,"허브 관리자 정보가 성공적으로 수정되었습니다."));
    }
}
