package com.example.wdmsystem.reservation.system;

import com.example.wdmsystem.customer.system.Customer;
import com.example.wdmsystem.customer.system.ICustomerRepository;
import com.example.wdmsystem.exception.InvalidInputException;
import com.example.wdmsystem.exception.NotFoundException;
import com.example.wdmsystem.service.system.IServiceRepository;
import com.example.wdmsystem.service.system.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ReservationService {
    private final IReservationRepository reservationRepository;
    private final ICustomerRepository customerRepository;
    private final IServiceRepository serviceRepository;

    public ReservationService(IReservationRepository reservationRepository, ICustomerRepository customerRepository, IServiceRepository serviceRepository) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.serviceRepository = serviceRepository;
    }

    public Reservation createReservation(ReservationDTO request) {
        if (request.customerId() == null || request.customerId() < 0) {
            throw new InvalidInputException("Customer id must be greater than 0");
        }
        if (request.serviceId() == null || request.serviceId() < 0) {
            throw new InvalidInputException("Service id must be greater than 0");
        }
        if (request.employeeId() == null || request.employeeId() < 0) {
            throw new InvalidInputException("Employee id must be greater than 0");
        }
        if (request.startTime() == null) {
            throw new InvalidInputException("Start time required");
        }
        if (request.endTime() == null) {
            throw new InvalidInputException("End time required");
        }
        if (request.sendConfirmation() == null) {
            throw new InvalidInputException("Send confirmation required");
        }

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        Service service = serviceRepository.findById(request.serviceId())
                .orElseThrow(() -> new NotFoundException("Service not found"));

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setService(service);
        reservation.setEmployeeId(request.employeeId());
        reservation.setStartTime(request.startTime());
        reservation.setEndTime(request.endTime());
        reservation.setSendConfirmation(request.sendConfirmation());

        if (request.reservationStatus() == null) {
            reservation.setReservation(ReservationStatus.CONFIRMED); // Default set to CONFIRMED ? maybe add PENDING ?
        }
        else {
            reservation.setReservation(request.reservationStatus());
        }

        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(null);

        return reservationRepository.save(reservation);
    }

    public Reservation getReservation(int reservationId) {
        if (reservationId < 0) {
            throw new InvalidInputException("Reservation id must be greater than 0");
        }

        Optional<Reservation> reservation = reservationRepository.findById(reservationId);

        if (reservation.isPresent()) {
            return reservation.get();
        }
        else{
            throw new NotFoundException("Reservation not found");
        }
    }

    @Transactional
    public void updateReservation(int reservationId, ReservationDTO request) {
        if (reservationId < 0) {
            throw new InvalidInputException("Reservation id must be greater than 0");
        }

        Optional<Reservation> reservation = reservationRepository.findById(reservationId);

        if (reservation.isEmpty()) {
            throw new NotFoundException("Reservation not found");
        }

        Reservation existingReservation = reservation.get();

//        if (request.customerId() != null) {
//            if (request.customerId() < 0) {
//                throw new InvalidInputException("Customer id must be greater than 0");
//            }
//
//            Customer customer = customerRepository.findById(request.customerId())
//                    .orElseThrow(() -> new NotFoundException("Customer not found"));
//
//            existingReservation.setCustomer(customer);
//        }
//        if (request.serviceId() != null) {
//            if (request.serviceId() < 0) {
//                throw new InvalidInputException("Service id must be greater than 0");
//            }
//
//            Service service = serviceRepository.findById(request.serviceId())
//                            .orElseThrow(() -> new NotFoundException("Service not found"));
//
//            existingReservation.setService(service);
//        }
        if (request.employeeId() != null) {
            if (request.employeeId() < 0) {
                throw new InvalidInputException("Employee id must be greater than 0");
            }
            existingReservation.setEmployeeId(request.employeeId());
        }
        if (request.startTime() != null) {
            existingReservation.setStartTime(request.startTime());
        }
        if (request.endTime() != null) {
            existingReservation.setEndTime(request.endTime());
        }
        if (request.reservationStatus() != null) {
            existingReservation.setReservation(request.reservationStatus());
        }
//        if (request.sendConfirmation() != null) {
//            existingReservation.setSendConfirmation(request.sendConfirmation());
//        }

        existingReservation.setUpdatedAt(LocalDateTime.now());

        reservationRepository.save(existingReservation);
    }

    public void deleteReservation(int reservationId) {
        if (reservationId < 0) {
            throw new InvalidInputException("Reservation id must be greater than 0");
        }

        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isEmpty()) {
            throw new NotFoundException("Reservation not found");
        }

        Reservation existingReservation = reservation.get();
        reservationRepository.delete(existingReservation);
    }

}
