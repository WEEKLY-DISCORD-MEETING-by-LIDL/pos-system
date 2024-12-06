package com.example.wdmsystem.reservation.system;

import com.example.wdmsystem.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReservationService {
    private final IReservationRepository reservationRepository;

    public ReservationService(IReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(ReservationDTO request) {
        Reservation reservation = new Reservation();
        reservation.setCustomerId(request.customerId());
        reservation.setServiceId(request.serviceId());
        reservation.setEmployeeId(request.employeeId());
        reservation.setStartTime(request.startTime());
        reservation.setSendConfirmation(request.sendConfirmation());
        reservation.setReservation(ReservationStatus.CONFIRMED); // Default set to CONFIRMED ? maybe add PENDING ?
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(null);

        return reservationRepository.save(reservation);
    }

    public Reservation getReservation(int reservationId) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        return reservation.orElse(null);
    }

    @Transactional
    public void updateReservation(int reservationId, ReservationDTO request) {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isEmpty()) {
            throw new NotFoundException("Reservation not found");
        }

        Reservation existingReservation = reservation.get();

        if(request.customerId() != null) {
            existingReservation.setCustomerId(request.customerId());
        }
        if(request.serviceId() != null) {
            existingReservation.setServiceId(request.serviceId());
        }
        if(request.employeeId() != null) {
            existingReservation.setEmployeeId(request.employeeId());
        }
        if(request.startTime() != null) {
            existingReservation.setStartTime(request.startTime());
        }
        if(request.endTime() != null) {
            existingReservation.setEndTime(request.endTime());
        }
        if(request.reservationStatus() != null) {
            existingReservation.setReservation(request.reservationStatus());
        }
        if(request.sendConfirmation() != null) {
            existingReservation.setSendConfirmation(request.sendConfirmation());
        }

        existingReservation.setUpdatedAt(LocalDateTime.now());

        reservationRepository.save(existingReservation);
    }

    public void deleteReservation(int reservationId) {
        reservationRepository.deleteById(reservationId);
    }

}
