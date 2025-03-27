package com.gaebal_easy.client.hub.infrastructure.db;

import com.gaebal_easy.client.hub.domain.entity.HubProductList;
import com.gaebal_easy.client.hub.domain.repository.HubProductListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class HubProductListImpl implements HubProductListRepository {

    private final HubProductListJpaRepository hubProductListJpaRepository;

    public Optional<HubProductList> getProduct(UUID id) {
        return hubProductListJpaRepository.findById(id);
    }

    @Override
    public Optional<HubProductList> refillProductAmount(UUID id, Long amount) {
        HubProductList hubProductList = hubProductListJpaRepository.findById(id).orElse(null);

        if (hubProductList != null) {
            log.info("재고 리필 {}", amount);
            hubProductList.updateAmount(amount);
            hubProductListJpaRepository.save(hubProductList);
        }
        return Optional.ofNullable(hubProductList);
    }

    @Override
    public Optional<HubProductList> decreseRealStock(UUID id, Long orderQuantity) {
        HubProductList hubProductList = getProduct(id).orElse(null);

        if (hubProductList != null) {
            hubProductList.updateAmount(hubProductList.getAmount() - orderQuantity);
            hubProductListJpaRepository.save(hubProductList);
        }

        return Optional.ofNullable(hubProductList);
    }
}
