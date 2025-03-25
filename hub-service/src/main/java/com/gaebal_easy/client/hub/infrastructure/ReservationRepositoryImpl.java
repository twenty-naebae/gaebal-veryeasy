package com.gaebal_easy.client.hub.infrastructure;

import com.gaebal_easy.client.hub.domain.entity.Reservation;
import com.gaebal_easy.client.hub.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;

    @Override
    public void save(Reservation reservation) {
        reservationJpaRepository.save(reservation);
    }


}
