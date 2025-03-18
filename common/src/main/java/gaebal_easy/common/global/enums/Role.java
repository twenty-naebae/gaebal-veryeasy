package gaebal_easy.common.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    MASTER("ROLE_MASTER"), //마스터
    HUB_MANAGER("ROLE_HUB_MANAGER"), //허브관리자
    HUB_DELIVERY_USER("ROLE_HUB_DELIVERY_USER"), // 허브 배송 담당자
    STORE_DELIVERY_USER("ROLE_STORE_DELIVERY_USER"), // 업체 배송 담당자
    STORE_MANAGER("ROLE_STORE"); //업체 담당자

    private final String roleName;

}
