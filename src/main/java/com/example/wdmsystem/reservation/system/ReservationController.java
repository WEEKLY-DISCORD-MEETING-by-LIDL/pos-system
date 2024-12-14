package com.example.wdmsystem.reservation.system;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
public final class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationDTO request) {
        Reservation createdReservation = reservationService.createReservation(request);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    @PutMapping("/reservations/{reservationId}")
    public ResponseEntity<String> updateReservation(@PathVariable int reservationId, @RequestBody ReservationDTO request) {
        reservationService.updateReservation(reservationId, request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/reservations/{reservationId}")
    public ResponseEntity<Reservation> getReservation(@PathVariable int reservationId) {
        Reservation reservation = reservationService.getReservation(reservationId);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<String> deleteReservation(@PathVariable int reservationId) {
        reservationService.deleteReservation(reservationId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}



