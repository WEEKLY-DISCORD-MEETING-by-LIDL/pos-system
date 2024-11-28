package com.example.wdmsystem.reservation.system;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationDTO {
    private int customerId;
    private int serviceId;
    private int employeeId;
    private LocalDateTime startTime;
    private boolean sendConfirmation;
    private LocalDateTime endTime;
    private String reservationStatus;
}
