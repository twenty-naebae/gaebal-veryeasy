package com.gaebal_easy.client.hub.application.service;

import com.gaebal_easy.client.hub.application.dto.HubLocationDto;
import com.gaebal_easy.client.hub.application.dto.HubResponseDto;
import com.gaebal_easy.client.hub.application.dto.ProductResponseDto;
import com.gaebal_easy.client.hub.application.dto.checkStockDto.CheckStockDto;
import com.gaebal_easy.client.hub.application.dto.checkStockDto.CheckStokProductDto;
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

import java.util.*;
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

    public HubLocationDto getCoordinate(String hubName) {
        List<Hub> hubList = hubRepository.findAll();
        HubLocationDto hubLocationDto = new HubLocationDto();
        for(Hub hub : hubList){
            String fourHubName = hub.getHubLocation().getName().substring(0,4);
            log.info("!!!!!!!!!fourHubName:!!!!!!!!! " + fourHubName);
            log.info("!!!!!!!!!hubName:!!!!!!!!! " + hubName);
            if(fourHubName.equals(hubName)) {
                hubLocationDto = new HubLocationDto(hub.getHubLocation().getLatitude(), hub.getHubLocation().getLongitude());
            }
        }
        log.info("hubLocationDto:!!!!!!!!! " + hubLocationDto.getLatitude() + " " + hubLocationDto.getLongitude());
        return hubLocationDto;
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
        Map<UUID, Long> productQuantities = new HashMap<>();
        ArrayList<RLock> locks = new ArrayList<>();
        boolean isEnoughStock = true;

        for(CheckStokProductDto product : stockCheckDto.getProducts()){
            productQuantities.put(product.getProductId(), product.getQuantity());
        }

        // 모든 상품에 대한 락 획득 시도
        try{
            // 상품 ID 기준으로 정렬하여 데드락 방지
            List<UUID> sortedProductIds = new ArrayList<>(productQuantities.keySet());
            Collections.sort(sortedProductIds);


            for(UUID productId : sortedProductIds){
                RLock lock = redissonClient.getLock("key:"+productId.toString());
                locks.add(lock);
                // 락 획득 시도
                boolean available = lock.tryLock(10, 5, TimeUnit.SECONDS);

                if (!available){
                    log.info("Lock {} 획득 실패", "key:"+productId.toString());
                    throw new IllegalStateException("Lock을 획득할 수 없습니다.");
                }

            }


            for(UUID productId : sortedProductIds){
                Long stock=0L;
                Long preemption=0L;
                // 먼저 캐시에 해당 상품의 재고 확인
                Cache stockCache = cacheManager.getCache("stock");
                Cache preemtionCache = cacheManager.getCache("preemption");

                if(preemtionCache.get("reserved:"+productId.toString(), String.class)==null) {
                    preemption = 0L;
                }

                // 캐싱 Hit
                if(stockCache.get(productId.toString(), String.class) != null){
                    log.info("캐싱 Hit!!!");
                    stock = Long.parseLong(stockCache.get(productId.toString(), String.class));

                    // 재고가 요청보다 부족
                    if( stock-preemption < productQuantities.get(productId)){
                        isEnoughStock = false;
                        throw new OutOfStockException(Code.OUT_OF_STOCK);
                    }
                }
                else {
                    // 캐싱 Miss
                    log.info("캐싱 Miss!!!");
                    ProductResponseDto findProduct = getProduct(productId, stockCheckDto.getHubId());

                    stock = findProduct.getAmount();

                    if( stock-preemption < productQuantities.get(productId)){
                        isEnoughStock = false;
                        throw new OutOfStockException(Code.OUT_OF_STOCK);
                    }
                    stockCache.put(productId.toString(), stock);
                }

            }


        } catch (InterruptedException e) {
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
                    log.info("상품: {}, 선점 수: {}",p.getProductId(), cnt+p.getQuantity());
                }
                log.info("예약정보 저장, 선점 저장");
            }

            // 획득한 모든 락 해제
            for (RLock lock : locks) {
                lock.unlock();
            }
        }


        return isEnoughStock;
    }



}
