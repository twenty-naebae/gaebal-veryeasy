package com.gaebal_easy.client.hub.presentation;

import com.gaebal_easy.client.hub.application.service.HubManagerService;
import com.gaebal_easy.client.hub.presentation.dto.HubManagerInfoResposne;
import gaebal_easy.common.global.dto.ApiResponseData;
import gaebal_easy.common.global.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
