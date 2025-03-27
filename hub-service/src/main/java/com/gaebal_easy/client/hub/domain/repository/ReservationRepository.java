package com.gaebal_easy.client.hub.domain.repository;

import com.gaebal_easy.client.hub.domain.entity.Reservation;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository {

    void save(Reservation reservation);

}
