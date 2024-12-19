package wdmsystem.reservation;

import wdmsystem.customer.Customer;
import wdmsystem.employee.Employee;
import wdmsystem.payment.Payment;
import wdmsystem.service.Service;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    public Employee employee;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public ReservationStatus reservationStatus;
    public boolean sendConfirmation;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "reservation")
    private List<Payment> payments;

    public Reservation(Customer customer, Service service, Employee employee, LocalDateTime startTime, LocalDateTime endTime,
                       ReservationStatus reservationStatus, boolean sendConfirmation, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.customer = customer;
        this.service = service;
        this.employee = employee;
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
