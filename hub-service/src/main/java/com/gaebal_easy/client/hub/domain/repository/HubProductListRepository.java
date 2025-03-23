package com.gaebal_easy.client.hub.domain.repository;

import com.gaebal_easy.client.hub.domain.entity.HubProduct;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HubProductListRepository {
    Optional<HubProduct> getProduct(UUID id);
}
