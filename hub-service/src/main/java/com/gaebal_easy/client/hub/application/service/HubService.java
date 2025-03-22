package com.gaebal_easy.client.hub.application.service;

import com.gaebal_easy.client.hub.application.dto.HubResponseDto;
import com.gaebal_easy.client.hub.application.dto.CheckStokProductDto;
import com.gaebal_easy.client.hub.application.dto.ProductResponseDto;
import com.gaebal_easy.client.hub.application.dto.CheckStockDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Boolean checkStock(CheckStockDto stockCheckDto){

        boolean isEnoughStock = true;
        for(CheckStokProductDto product : stockCheckDto.getProducts()){
            Long stock=0L;
            Long preemption=0L;

            // product별로 락 획득
            RLock lock = redissonClient.getLock("key:"+product.getProductId().toString());
            log.info("재고확인 요청 Product: {}, 요청수량: {}",product.getProductId(), product.getQuantity());
            try{
                // 락 획득 시도
                boolean available = lock.tryLock(10, 5, TimeUnit.SECONDS);

                if (!available){
                    log.info("Lock {} 획득 실패", "key:"+product.getProductId().toString());
                    throw new IllegalStateException("Lock을 획득할 수 없습니다.");
                }

                // 먼저 캐시에 해당 상품의 재고 확인
                Cache stockCache = cacheManager.getCache("stock");
                Cache preemtionCache = cacheManager.getCache("preemption");

                if(preemtionCache.get("reserved:"+product.getProductId().toString(), String.class)==null) {
                    preemption = 0L;
                }

                // 캐싱 Hit
                if(stockCache.get(product.getProductId().toString(), String.class) != null){
                    log.info("캐싱 Hit!!!");
                    stock = Long.parseLong(stockCache.get(product.getProductId().toString(), String.class));

                    // 재고가 요청보다 부족
                    if( stock-preemption < product.getQuantity()){
                        isEnoughStock = false;
                        throw new OutOfStockException(Code.OUT_OF_STOCK);
                    }
                }
                else {
                    // 캐싱 Miss
                    log.info("캐싱 Miss!!!");
                    ProductResponseDto findProduct = getProduct(product.getProductId(), stockCheckDto.getHubId());

                    stock = findProduct.getAmount();

                    if (stock-preemption < product.getQuantity()) {
                        isEnoughStock = false;
                        throw new OutOfStockException(Code.OUT_OF_STOCK);
                    }
                    stockCache.put(product.getProductId().toString(), stock);
                }


            } catch (InterruptedException e) {
                log.info("실패 : Product: {}, 재고: {}, 요청수량: {}",product.getProductId(), stock-preemption, product.getQuantity());
                throw new OrderFailExceiption(Code.ORDER_FAIL_EXCEIPTION);
            }finally {

                if(isEnoughStock){
                    Cache reservationCache = cacheManager.getCache("reservation");
                    Cache preemptionCache = cacheManager.getCache("preemption");

                    // 예약정보 캐시 저장
                    String reservationId = UUID.randomUUID().toString();
                    reservationCache.put(reservationId, stockCheckDto);

                    // 선점 재고 수량 캐시 저장
                    for(CheckStokProductDto p : stockCheckDto.getProducts()){
                        Long cnt = 0L;
                        if(preemptionCache.get("reserved:" + p.getProductId().toString()) != null){
                            cnt = Long.parseLong(preemptionCache.get("reserved:" + p.getProductId().toString(), String.class));
                        }
                        preemptionCache.put("reserved:"+p.getProductId().toString(), cnt + p.getQuantity());
                    }
                    log.info("예약정보 저장, 선점 저장");
                }
                lock.unlock();
            }
        }


        return isEnoughStock;
    }


}
