package wdmsystem.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO request) {
        log.info("Received request to create reservation: {}", request);
        ReservationDTO createdReservation = reservationService.createReservation(request);
        log.info("Reservation created successfully: {}", createdReservation);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }

    @PutMapping("/reservations/{reservationId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @reservationService.isOwnedByCurrentUser(#reservationId))")
    public ResponseEntity<String> updateReservation(@PathVariable int reservationId, @RequestBody ReservationDTO request) {
        log.info("Received request to update reservation with ID {}: {}", reservationId, request);
        reservationService.updateReservation(reservationId, request);
        log.info("Reservation with ID {} updated successfully", reservationId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/reservations/{reservationId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @reservationService.isOwnedByCurrentUser(#reservationId))")
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable int reservationId) {
        log.info("Received request to fetch reservation with ID {}", reservationId);
        ReservationDTO reservation = reservationService.getReservation(reservationId);
        log.info("Fetched reservation details: {}", reservation);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @GetMapping("/reservations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    public ResponseEntity<List<ReservationDTO>> getReservation(@RequestParam(required = false) boolean upcoming, @RequestParam(required = false) Integer limit) {
        List<ReservationDTO> reservations = reservationService.getReservations(upcoming, limit);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @DeleteMapping("/reservations/{reservationId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @reservationService.isOwnedByCurrentUser(#reservationId))")
    public ResponseEntity<String> deleteReservation(@PathVariable int reservationId) {
        log.info("Received request to delete reservation with ID {}", reservationId);
        reservationService.deleteReservation(reservationId);
        log.info("Reservation with ID {} deleted successfully", reservationId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/reservations/{reservationId}/cancel")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @reservationService.isOwnedByCurrentUser(#reservationId))")
    public ResponseEntity<String> cancelReservation(@PathVariable int reservationId) {
        log.info("Received request to cancel reservation with ID {}", reservationId);
        reservationService.cancelReservation(reservationId);
        log.info("Reservation with ID {} canceled successfully", reservationId);
        return new ResponseEntity<>("Reservation canceled successfully", HttpStatus.OK);
    }

    //new
    @GetMapping("/reservations/{reservationId}/unpaid-price")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderService.isOwnedByCurrentUser(#reservationId))")
    public ResponseEntity<Double> getUnpaidPrice(@PathVariable int reservationId) {
        double price = reservationService.getUnpaidPrice(reservationId);
        return new ResponseEntity<>(price, HttpStatus.OK);
    }

}
