package com.example.wdmsystem.reservation.system;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public final class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public int customerId;
    public int serviceId;
    public int employeeId;
    public LocalDateTime startTime; //using LocalDateTime, not Date because its just better lol
    public LocalDateTime endTime;
    public ReservationStatus reservation;
    public boolean sendConfirmation;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Reservation(Integer id, int customerId, int serviceId, int employeeId, LocalDateTime startTime, LocalDateTime endTime, ReservationStatus reservation, boolean sendConfirmation, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.serviceId = serviceId;
        this.employeeId = employeeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservation = reservation;
        this.sendConfirmation = sendConfirmation;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //@Entity required constructor
    public Reservation() {
    }
}
