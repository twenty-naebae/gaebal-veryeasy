package com.gaebal_easy.client.hub.application.service;

import com.gaebal_easy.client.hub.application.dto.CheckStokProductDto;
import com.gaebal_easy.client.hub.application.dto.CheckStockDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class HubStockTest {

    @Autowired
    private HubService hubService;

    @Autowired
    CacheManager cacheManager;


    @Test
    @DisplayName("dtest")
    @Transactional
    void test() throws InterruptedException {
        int thred =100;

        ExecutorService executor = Executors.newFixedThreadPool(thred);
        CountDownLatch latch = new CountDownLatch(thred);


        for (int i = 0; i < thred; i++) {
            CheckStockDto check = null;
            if((i%2)==0) {
                List<CheckStokProductDto> products = new ArrayList<>();
                CheckStokProductDto product1 = CheckStokProductDto.builder()
                        .productId(UUID.fromString("6bc4dbbc-05d2-11f0-82d4-0242ac110004"))
                        .quantity(20L)
                        .build();

                CheckStokProductDto product2 = CheckStokProductDto.builder()
                        .productId(UUID.fromString("6bc5a25a-05d2-11f0-82d4-0242ac110004"))
                        .quantity(10L)
                        .build();

                products.add(product1);
                products.add(product2);

                check = CheckStockDto.builder()
                        .hubId(UUID.fromString("3479b1c5-05d2-11f0-82d4-0242ac110004"))
                        .products(products)
                        .build();
            }else{
                List<CheckStokProductDto> products = new ArrayList<>();
                CheckStokProductDto product1 = CheckStokProductDto.builder()
                        .productId(UUID.fromString("6bc4dbbc-05d2-11f0-82d4-0242ac110004"))
                        .quantity(10L)
                        .build();

                CheckStokProductDto product2 = CheckStokProductDto.builder()
                        .productId(UUID.fromString("6bc5a25a-05d2-11f0-82d4-0242ac110004"))
                        .quantity(20L)
                        .build();

                products.add(product2);
                products.add(product1);

                check = CheckStockDto.builder()
                        .hubId(UUID.fromString("3479b1c5-05d2-11f0-82d4-0242ac110004"))
                        .products(products)
                        .build();
            }

            CheckStockDto finalCheck = check;
            executor.submit(() -> {
                try {
                    hubService.checkStock(finalCheck);
                } catch (Exception e) {
                    Assertions.assertThat("현재 해당 상품의 재고가 없습니다.").isEqualTo(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Cache stockCache = cacheManager.getCache("productStockCache");

        org.junit.jupiter.api.Assertions.assertAll(
                () -> Assertions.assertThat(stockCache.get("6bc4dbbc-05d2-11f0-82d4-0242ac110004", Long.class)).isEqualTo(8500L),
                () -> Assertions.assertThat(stockCache.get("6bc5a25a-05d2-11f0-82d4-0242ac110004", Long.class)).isEqualTo(18500L)
        );
    }




    @Test
    @DisplayName("여러상품 주문 시 하나라도 재고 부족할 시 테스트")
    @Transactional
    void AllOrNothingTest() throws InterruptedException {
        int thred =1;

        ExecutorService executor = Executors.newFixedThreadPool(thred);
        CountDownLatch latch = new CountDownLatch(thred);


        for (int i = 0; i < thred; i++) {

            List<CheckStokProductDto> products = new ArrayList<>();
            CheckStokProductDto product1 = CheckStokProductDto.builder()
                    .productId(UUID.fromString("6bc4dbbc-05d2-11f0-82d4-0242ac110004"))
                    .quantity(10L)
                    .build();

            CheckStokProductDto product2 = CheckStokProductDto.builder()
                    .productId(UUID.fromString("6bc5a25a-05d2-11f0-82d4-0242ac110004"))
                    .quantity(20001L)
                    .build();

            products.add(product1);
            products.add(product2);

            CheckStockDto check = CheckStockDto.builder()
                    .hubId(UUID.fromString("3479b1c5-05d2-11f0-82d4-0242ac110004"))
                    .products(products)
                    .build();

            executor.submit(() -> {
                try {
                    hubService.checkStock(check);
                } catch (Exception e) {
                    Assertions.assertThat("현재 해당 상품의 재고가 없습니다.").isEqualTo(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

    }




    @Test
    @DisplayName("여러 사람 한 상품 재고부족 테스트")
    @Transactional
    void stockTest() throws InterruptedException {
        int thred =2;

        ExecutorService executor = Executors.newFixedThreadPool(thred);
        CountDownLatch latch = new CountDownLatch(thred);


        for (int i = 0; i < thred; i++) {
            long quantity = 0;
            if(i==0) quantity = 5001;
            else quantity = 5000;

            List<CheckStokProductDto> products = new ArrayList<>();
            CheckStokProductDto product1 = CheckStokProductDto.builder()
                    .productId(UUID.fromString("6bc4dbbc-05d2-11f0-82d4-0242ac110004"))
                    .quantity(quantity)
                    .build();

            products.add(product1);

            CheckStockDto check = CheckStockDto.builder()
                    .hubId(UUID.fromString("3479b1c5-05d2-11f0-82d4-0242ac110004"))
                    .products(products)
                    .build();

            executor.submit(() -> {
                try {
                    hubService.checkStock(check);
                } catch (Exception e) {
                    Assertions.assertThat("현재 해당 상품의 재고가 없습니다.").isEqualTo(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

    }


    @Test
    @DisplayName("동시구매 테스트")
    @Transactional
    void outOfStockTest() throws InterruptedException {
        int thred =1000;

        ExecutorService executor = Executors.newFixedThreadPool(thred);
        CountDownLatch latch = new CountDownLatch(thred);

        List<CheckStokProductDto> products = new ArrayList<>();
        CheckStokProductDto product1 = CheckStokProductDto.builder()
                .productId(UUID.fromString("6bc4dbbc-05d2-11f0-82d4-0242ac110004"))
                .quantity(10L)
                .build();

        products.add(product1);

        CheckStockDto check = CheckStockDto.builder()
                .hubId(UUID.fromString("3479b1c5-05d2-11f0-82d4-0242ac110004"))
                .products(products)
                .build();

        for (int i = 0; i < thred; i++) {
            executor.submit(() -> {
                try {
                    hubService.checkStock(check);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        Cache stockCache = cacheManager.getCache("stock");
        Cache preemptionCache = cacheManager.getCache("preemption");
        Long stock = Long.parseLong(stockCache.get("6bc4dbbc-05d2-11f0-82d4-0242ac110004", String.class));
        Long preemption = Long.parseLong(preemptionCache.get("reserved:"+"6bc4dbbc-05d2-11f0-82d4-0242ac110004", String.class));
        Assertions.assertThat(stock-preemption).isEqualTo(0L);

    }

}