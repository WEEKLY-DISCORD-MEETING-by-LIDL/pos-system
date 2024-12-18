package wdmsystem.reservation;

import java.time.LocalDateTime;

public record ReservationDTO (Integer id, Integer customerId, Integer serviceId, Integer employeeId, LocalDateTime startTime,
                              Boolean sendConfirmation, LocalDateTime endTime, ReservationStatus reservationStatus) {

}
