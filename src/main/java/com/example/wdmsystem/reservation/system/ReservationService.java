package com.example.wdmsystem.reservation.system;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public final class ReservationService {
    private final IReservationRepository reservationRepository;

    public ReservationService(IReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(ReservationDTO request) {
        Reservation reservation = new Reservation();
        reservation.setCustomerId(request.getCustomerId());
        reservation.setServiceId(request.getServiceId());
        reservation.setEmployeeId(request.getEmployeeId());
        reservation.setStartTime(request.getStartTime());
        reservation.setSendConfirmation(request.isSendConfirmation());
        reservation.setReservation(ReservationStatus.CONFIRMED); // Default set to CONFIRMED ? maybe add PENDING ?
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        // TODO: Save reservation to the repository (currently returns null)
        return null;
    }

    public Reservation getReservation(int reservationId) {
        // TODO: Fetch the reservation from the repository
        return null;
    }

    public void updateReservation(int reservationId, ReservationDTO request) {
        // TODO: Fetch the reservation, update fields, and save back to repository
    }

    public void deleteReservation(int reservationId) {
        // TODO: Delete the reservation from the repository
    }

}
