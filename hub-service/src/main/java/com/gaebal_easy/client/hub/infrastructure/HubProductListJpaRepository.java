package com.gaebal_easy.client.hub.infrastructure;

import com.gaebal_easy.client.hub.domain.entity.HubProductList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HubProductListJpaRepository extends JpaRepository<HubProductList, UUID> {

}
