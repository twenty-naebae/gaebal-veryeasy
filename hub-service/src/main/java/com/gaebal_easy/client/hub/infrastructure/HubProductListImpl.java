package com.gaebal_easy.client.hub.infrastructure;

import com.gaebal_easy.client.hub.domain.entity.HubProductList;
import com.gaebal_easy.client.hub.domain.repository.HubProductListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubProductListImpl implements HubProductListRepository {

    private final HubProductListJpaRepository hubProductListJpaRepository;

    public Optional<HubProductList> getProduct(UUID id) {
        return hubProductListJpaRepository.findById(id);
    }
}
