package com.gaebal_easy.client.hub.infrastructure.db;

import com.gaebal_easy.client.hub.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, UUID> {

}
