package com.example.wdmsystem.customer.system;


import com.example.wdmsystem.exception.InvalidInputException;
import com.example.wdmsystem.exception.NotFoundException;
import com.example.wdmsystem.reservation.system.IReservationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.wdmsystem.reservation.system.Reservation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService {
    private final ICustomerRepository customerRepository;
    private final IReservationRepository reservationRepository;

    public CustomerService(ICustomerRepository customerRepository, IReservationRepository reservationRepository) {
        this.customerRepository = customerRepository;
        this.reservationRepository = reservationRepository;
    }

    public Customer createCustomer(CustomerDTO createCustomer) {
        Customer customer = new Customer(
                1,
                createCustomer.firstName(),
                createCustomer.lastName(),
                createCustomer.phone(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        return customerRepository.save(customer);
    }

    public List<Customer> getCustomers(LocalDateTime createdAtMin, LocalDateTime createdAtMax, Integer limit) {
        if (createdAtMin != null && createdAtMax != null && createdAtMin.isAfter(createdAtMax)) {
            throw new InvalidInputException("createdAtMin must be earlier than createdAtMax.");
        }

        LocalDateTime lowerDate = LocalDateTime.of(0,1,1,0,1);
        LocalDateTime upperDate = LocalDateTime.of(9999,12,30,23,59);
        if (createdAtMin != null) {
            lowerDate = createdAtMin;
        }
        if (createdAtMax != null) {
            upperDate = createdAtMax;
        }

        if (limit == null || limit <= 0 || limit > 250) {
            limit = 50;
        }
        PageRequest pageRequest = PageRequest.of(0, limit);
        return customerRepository.findCustomersWithinDateRange(lowerDate, upperDate, pageRequest);
    }

    public Customer getCustomer(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));
    }

    @Transactional
    public void updateCustomer(int id, CustomerDTO request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));

        if (request.firstName() != null) {
            if (request.firstName().length() > 30) {
                throw new InvalidInputException("First name must be less than 30 characters");
            }
            customer.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            if (request.lastName().length() > 30) {
                throw new InvalidInputException("Last name must be less than 30 characters");
            }
            customer.setLastName(request.lastName());
        }
        if (request.phone() != null) {
            customer.setPhone(request.phone());
        }

        customer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

    public void deleteCustomer(int id) {
        if (!customerRepository.existsById(id)) {
            throw new NotFoundException("Customer with id " + id + " not found");
        }

        customerRepository.deleteById(id);
    }

    public List<Reservation> getCustomerReservations(int customerId, boolean upcoming, Integer limit) {
        if (limit == null || limit <= 0 || limit > 250) {
            limit = 50;
        }
        if (!customerRepository.existsById(customerId)) {
            throw new NotFoundException("Customer with ID " + customerId + " does not exist");
        }

        LocalDateTime upcomingDate = LocalDateTime.now();
        if (!upcoming) {
            upcomingDate = LocalDateTime.of(0,1,1,0,1);
        }
        PageRequest pageRequest = PageRequest.of(0, limit);

        return reservationRepository.findReservationByCustomerId(customerId, upcomingDate, pageRequest);
    }
}
