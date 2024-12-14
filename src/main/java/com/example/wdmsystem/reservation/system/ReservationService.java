package com.example.wdmsystem.reservation.system;

import com.example.wdmsystem.customer.system.Customer;
import com.example.wdmsystem.customer.system.ICustomerRepository;
import com.example.wdmsystem.employee.system.IEmployeeRepository;
import com.example.wdmsystem.exception.NotFoundException;
import com.example.wdmsystem.service.system.IServiceRepository;
import com.example.wdmsystem.service.system.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@org.springframework.stereotype.Service
public class ReservationService {
    private final IReservationRepository reservationRepository;
    private final ICustomerRepository customerRepository;
    private final IServiceRepository serviceRepository;
    private final IEmployeeRepository employeeRepository;

    public ReservationService(IReservationRepository reservationRepository, ICustomerRepository customerRepository,
                              IServiceRepository serviceRepository, IEmployeeRepository employeeRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.serviceRepository = serviceRepository;
        this.employeeRepository = employeeRepository;
    }

    public Reservation createReservation(ReservationDTO request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        Service service = serviceRepository.findById(request.serviceId())
                .orElseThrow(() -> new NotFoundException("Service not found"));

        if (!employeeRepository.existsById(request.employeeId())) {
            throw new NotFoundException("Employee not found");
        }

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setService(service);
        reservation.setEmployeeId(request.employeeId());
        reservation.setStartTime(request.startTime());
        reservation.setEndTime(request.endTime());
        reservation.setSendConfirmation(request.sendConfirmation());

        if (request.reservationStatus() == null) {
            reservation.setReservationStatus(ReservationStatus.CONFIRMED); // Default set to CONFIRMED ? maybe add PENDING ?
        }
        else {
            reservation.setReservationStatus(request.reservationStatus());
        }

        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());

        return reservationRepository.save(reservation);
    }

    public Reservation getReservation(int reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));
    }

    @Transactional
    public void updateReservation(int reservationId, ReservationDTO request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        if (request.employeeId() != null) {
            if (!employeeRepository.existsById(request.employeeId())) {
                throw new NotFoundException("Employee not found");
            }
            reservation.setEmployeeId(request.employeeId());
        }
        if (request.startTime() != null) {
            reservation.setStartTime(request.startTime());
        }
        if (request.endTime() != null) {
            reservation.setEndTime(request.endTime());
        }
        if (request.reservationStatus() != null) {
            reservation.setReservationStatus(request.reservationStatus());
        }

        reservation.setUpdatedAt(LocalDateTime.now());

        reservationRepository.save(reservation);
    }

    public void cancelReservation(int reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found"));

        reservation.setReservationStatus(ReservationStatus.CANCELLED);

        reservation.setUpdatedAt(LocalDateTime.now());
    }

    public void deleteReservation(int reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new NotFoundException("Reservation not found");
        }

        reservationRepository.deleteById(reservationId);
    }

}
