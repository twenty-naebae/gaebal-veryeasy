package com.gaebal_easy.client.hub.infrastructure;

import com.gaebal_easy.client.hub.domain.entity.HubProductList;
import com.gaebal_easy.client.hub.domain.repository.HubProductListRepository;
import gaebal_easy.common.global.exception.Code;
import gaebal_easy.common.global.exception.ProductNotFoundException;
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

    @Override
    public Optional<HubProductList> refillProductAmount(UUID id, Long amount) {
        HubProductList hubProductList = hubProductListJpaRepository.findById(id).orElse(null);

        if (hubProductList != null) {
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
