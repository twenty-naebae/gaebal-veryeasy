package com.gaebal_easy.client.hub.application.service;

import com.gaebal_easy.client.hub.application.dto.HubResponseDto;
import com.gaebal_easy.client.hub.application.dto.CheckStokProductDto;
import com.gaebal_easy.client.hub.application.dto.ProductResponseDto;
import com.gaebal_easy.client.hub.application.dto.StockCheckDto;
import com.gaebal_easy.client.hub.domain.entity.Hub;
import com.gaebal_easy.client.hub.domain.entity.HubProductList;
import com.gaebal_easy.client.hub.domain.repository.HubProductListRepository;
import com.gaebal_easy.client.hub.domain.repository.HubRepository;
import gaebal_easy.common.global.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubService {

    private final HubRepository hubRepository;
    private final HubProductListRepository hubProductListRepository;
    private final RedissonClient redissonClient;
    private final CacheManager cacheManager;


    public ProductResponseDto getProduct(UUID productId, UUID hubId) {
        Hub hub = getHub(hubId);
        HubProductList hubProductList = getHubProductList(productId);
        if(!hubProductList.getHub().getId().equals(hub.getId()))
            throw new CanNotFindProductInHubException(Code.HUB_CAN_NOT_FIND_PRODUCT_IN_HUB);
        return ProductResponseDto.of(hubProductList,hub);
    }

    public void deleteHub(UUID id) {
        Hub hub = getHub(id);
        // TODO : 유저네임 넘겨주기, 이미 삭제된 허브가 아닌지 판별 deletedAtIsNotNull
        hub.delete("deleted by");
        hubRepository.save(hub);
    }

    public HubResponseDto requireHub(UUID id) {
        Hub hub = getHub(id);
        return HubResponseDto.of(hub);
    }

    private Hub getHub(UUID id){
        return hubRepository.getHub(id).orElseThrow(() -> new HubNotFoundException(Code.HUB_NOT_FOUND));
    }
    private HubProductList getHubProductList(UUID id){
        return hubProductListRepository.getProduct(id).orElseThrow(() -> new ProductNotFoundException(Code.HUB_PRODUCT_NOT_FOUND));
    }



    @Transactional
    public Boolean checkStock(StockCheckDto stockCheckDto){

        boolean possibleStock = true;
        for(CheckStokProductDto product : stockCheckDto.getProducts()){
            log.info("Product: {}, 수량: {}",product.getProductId(), product.getQuantity());
            // product별로 락 획득
            RLock lock = redissonClient.getLock("key:"+product.getProductId().toString());
            try{
                // 락 획득 시도
                boolean available = lock.tryLock(5, 5, TimeUnit.SECONDS);

                if (!available){
                    log.info("Lock {} 획득 실패", "key:"+product.getProductId().toString());
                    throw new IllegalStateException("Lock을 획득할 수 없습니다.");
                }

                log.info("재고 확인 로직");
                // 먼저 캐시에 해당 상품의 재고 확인
                Cache stockCache = cacheManager.getCache("productStockCache");
                Long stock = stockCache.get(product.getProductId().toString(), Long.class);

                // 캐싱 Hit
                if(stock != null){
                    log.info("캐싱 Hit!!!");

                    // 재고가 요청보다 부족
                    if(stock< product.getQuantity()){
                        possibleStock = false;
                        throw new OutOfStockException(Code.OUT_OF_STOCK);
                    }
                    // 재고 여유(주문에 대해 재고 선점 : 캐시에서 재고 차감, 이 후 주문이 완료되면 mariaDB에서 재고 차감 반영)
                    stockCache.put(product.getProductId().toString(), stock-product.getQuantity());
                }

                // 캐싱 Miss
                log.info("캐싱 Miss!!!");
                ProductResponseDto findProduct = getProduct(product.getProductId(), stockCheckDto.getHubId());

                stock = findProduct.getAmount();

                if(stock< product.getQuantity()){
                    possibleStock = false;
                    throw new OutOfStockException(Code.OUT_OF_STOCK);
                }
                stockCache.put(product.getProductId().toString(), stock-product.getQuantity());

            } catch (InterruptedException e) {
                throw new RuntimeException(e);

            }finally {
                lock.unlock();
            }
        }
        return possibleStock;
    }


}
