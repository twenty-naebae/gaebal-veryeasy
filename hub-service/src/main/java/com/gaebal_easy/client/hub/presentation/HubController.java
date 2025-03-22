package com.gaebal_easy.client.hub.presentation;

import com.gaebal_easy.client.hub.application.dto.CheckStockDto;
import com.gaebal_easy.client.hub.application.service.HubService;
import com.gaebal_easy.client.hub.presentation.dto.HubCreateRequestDto;
import com.gaebal_easy.client.hub.presentation.dto.HubRequestDto;
import gaebal_easy.common.global.dto.ApiResponseData;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/hub-service/api")
@RequiredArgsConstructor
@Slf4j
public class HubController {

    private final HubService hubService;

    @GetMapping("/getProduct")
    public ResponseEntity<?> requestGetProduct(@RequestParam UUID productId,
                                               @RequestParam UUID hubId) {
        return ResponseEntity.ok(ApiResponseData.success(hubService.getProduct(productId,hubId), "허브에 해당 상품이 존재합니다."));
    }

    //TODO : 헤더에 로그인 유저 전달, deleteHub 메서드에 유저 정보 전달 추가
//    @PreAuthorize("hasRole('MASTER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHub(@PathVariable UUID id) {
        hubService.deleteHub(id);
        return ResponseEntity.ok(ApiResponseData.success(null));
    }

//    @PreAuthorize("hasRole('MASTER')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateHub(@PathVariable UUID id, @RequestBody(required = false) HubRequestDto hubRequestDto) {
        return ResponseEntity.ok(ApiResponseData.success(null,"담당자가 확인 후 수정하겠습니다."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> requireHub(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponseData.success(hubService.requireHub(id)));
    }
    @PostMapping
    public ResponseEntity<?> createHub(@RequestBody HubCreateRequestDto hubCreateRequestDto) {
        return ResponseEntity.ok(ApiResponseData.success(null,"담당자가 확인 후 허브를 추가하겠습니다."));
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok(ApiResponseData.success("Hello"));
    }


    @PostMapping("/products/stock")
    public ResponseEntity<?> checkStock(@RequestBody CheckStockDto stockCheckDto) {
        log.info("stockCheck {}", stockCheckDto.toString());
        Boolean possibleStock = hubService.checkStock(stockCheckDto);
        return ResponseEntity.ok(ApiResponseData.success(possibleStock));
    }


}
