package com.gaebal_easy.delivery.service;

import com.gaebal_easy.delivery.application.service.DeliveryUserAssignmentService;
import com.gaebal_easy.delivery.domain.entity.HubDeliveryUser;
import com.gaebal_easy.delivery.domain.entity.StoreDeliveryUser;
import com.gaebal_easy.delivery.domain.repository.HubDeliveryUserRepository;
import com.gaebal_easy.delivery.domain.repository.StoreDeliveryUserRepository;
import com.gaebal_easy.delivery.infrastructure.redis.RedisDeliveryUserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DeliveryUserAssignmentServiceIntegrationTest {

    @Autowired
    private DeliveryUserAssignmentService deliveryUserAssignmentService;

    @Autowired
    private RedisDeliveryUserUtil redisDeliveryUserUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private HubDeliveryUserRepository hubDeliveryUserRepository;

    @Autowired
    private StoreDeliveryUserRepository storeDeliveryUserRepository;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 준비
        HubDeliveryUser hubUser1 = hubDeliveryUserRepository.save(
                HubDeliveryUser.of(101L, "테스트허브1", "slack-hub", 0)
        );
        HubDeliveryUser hubUser2 = hubDeliveryUserRepository.save(
                HubDeliveryUser.of(102L, "테스트허브2", "slack-hub2", 1)
        );
        HubDeliveryUser hubUser3 = hubDeliveryUserRepository.save(
                HubDeliveryUser.of(103L, "테스트허브3", "slack-hub3", 2)
        );
        redisDeliveryUserUtil.addToHubDeliveryUserList(hubUser1.getUserId());
        redisDeliveryUserUtil.addToHubDeliveryUserList(hubUser2.getUserId());
        redisDeliveryUserUtil.addToHubDeliveryUserList(hubUser3.getUserId());

        StoreDeliveryUser storeUser1 = storeDeliveryUserRepository.save(
                StoreDeliveryUser.of(201L, "테스트업체1", "slack-store1", "1",0));
        StoreDeliveryUser storeUser2 = storeDeliveryUserRepository.save(
                StoreDeliveryUser.of(202L, "테스트업체2", "slack-store2", "1",1));
        StoreDeliveryUser storeUser3 = storeDeliveryUserRepository.save(
                StoreDeliveryUser.of(203L, "테스트업체3", "slack-store3", "1",2));

        redisDeliveryUserUtil.addToStoreDeliveryUserList(storeUser1.getUserId(), storeUser1.getHubId());
        redisDeliveryUserUtil.addToStoreDeliveryUserList(storeUser1.getUserId(), storeUser2.getHubId());
        redisDeliveryUserUtil.addToStoreDeliveryUserList(storeUser1.getUserId(), storeUser3.getHubId());
    }

    @Test
    void hub_delivery_user_assignment_success() {
        HubDeliveryUser user1 = deliveryUserAssignmentService.assignHubDeliveryUser();
        HubDeliveryUser user2 = deliveryUserAssignmentService.assignHubDeliveryUser();
        HubDeliveryUser user3 = deliveryUserAssignmentService.assignHubDeliveryUser();
        HubDeliveryUser user4 = deliveryUserAssignmentService.assignHubDeliveryUser();
        assertThat(user4).isNotNull();
        assertThat(user4.getName()).isEqualTo("테스트허브1");
    }

    @Test
    void store_delivery_user_assignment_success() {
        StoreDeliveryUser user1 = deliveryUserAssignmentService.assignStoreDeliveryUser(1L);
        StoreDeliveryUser user2 = deliveryUserAssignmentService.assignStoreDeliveryUser(1L);
        StoreDeliveryUser use3 = deliveryUserAssignmentService.assignStoreDeliveryUser(1L);
        StoreDeliveryUser user4 = deliveryUserAssignmentService.assignStoreDeliveryUser(1L);
        StoreDeliveryUser user5 = deliveryUserAssignmentService.assignStoreDeliveryUser(1L);
        assertThat(user5).isNotNull();
        assertThat(user5.getName()).isEqualTo("테스트업체2");
        assertThat(user5.getHubId()).isEqualTo(1);
    }
}
