package gaebal_easy.common.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    MASTER("마스터"),
    HUB_MANAGER("허브관리자"),
    DELIVERY_USER("배송 담당자"),
    COMPANY_USER("업체 담당자");

    private final String roleName;
}
