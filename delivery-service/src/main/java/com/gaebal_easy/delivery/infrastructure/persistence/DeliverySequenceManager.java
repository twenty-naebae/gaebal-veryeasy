package com.gaebal_easy.delivery.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliverySequenceManager {

    @PersistenceContext
    private final EntityManager entityManager;

    public int getNextHubDeliveryOrder() {
        return ((Number) entityManager
                .createNativeQuery("SELECT NEXT VALUE FOR hub_delivery_order_seq")
                .getSingleResult()).intValue();
    }

    public int getNextStoreDeliveryOrder() {
        return ((Number) entityManager
                .createNativeQuery("SELECT NEXT VALUE FOR store_delivery_order_seq")
                .getSingleResult()).intValue();
    }
}
