package com.gaebal_easy.hub.domain.repository;

import com.gaebal_easy.hub.domain.entity.HubTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// 테스트용
@Repository
public interface HubTempRepository extends JpaRepository<HubTemp, UUID> {

    public HubTemp findByHubName(String hubName);
}
