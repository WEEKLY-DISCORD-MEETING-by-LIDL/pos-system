package wdmsystem.reservation.system;

import wdmsystem.customer.system.Customer;
import wdmsystem.service.system.Service;
import jakarta.persistence.*;
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
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    public Customer customer;
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    public Service service;
    public int employeeId;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public ReservationStatus reservationStatus;
    public boolean sendConfirmation;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Reservation(Customer customer, Service service, int employeeId, LocalDateTime startTime, LocalDateTime endTime,
                       ReservationStatus reservationStatus, boolean sendConfirmation, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.customer = customer;
        this.service = service;
        this.employeeId = employeeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reservationStatus = reservationStatus;
        this.sendConfirmation = sendConfirmation;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //@Entity required constructor
    public Reservation() {
    }
}
