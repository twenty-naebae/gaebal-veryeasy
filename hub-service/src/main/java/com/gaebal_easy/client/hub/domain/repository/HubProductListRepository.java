package com.gaebal_easy.client.hub.domain.repository;

import com.gaebal_easy.client.hub.domain.entity.HubProductList;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HubProductListRepository {
    Optional<HubProductList> getProduct(UUID id);
}
