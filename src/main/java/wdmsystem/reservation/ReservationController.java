package wdmsystem.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO request) {
        ReservationDTO createdReservation = reservationService.createReservation(request);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    @PutMapping("/reservations/{reservationId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @reservationService.isOwnedByCurrentUser(#reservationId))")
    public ResponseEntity<String> updateReservation(@PathVariable int reservationId, @RequestBody ReservationDTO request) {
        reservationService.updateReservation(reservationId, request);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/reservations/{reservationId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @reservationService.isOwnedByCurrentUser(#reservationId))")
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable int reservationId) {
        ReservationDTO reservation = reservationService.getReservation(reservationId);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @DeleteMapping("/reservations/{reservationId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @reservationService.isOwnedByCurrentUser(#reservationId))")
    public ResponseEntity<String> deleteReservation(@PathVariable int reservationId) {
        reservationService.deleteReservation(reservationId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
  
    @PutMapping("/reservations/{reservationId}/cancel")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @reservationService.isOwnedByCurrentUser(#reservationId))")
    public ResponseEntity<String> cancelReservation(@PathVariable int reservationId) {
        reservationService.cancelReservation(reservationId);
        return new ResponseEntity<>("Reservation canceled successfully", HttpStatus.OK);
    }


}



