package com.example.wdmsystem.customer.system;


import com.example.wdmsystem.exception.InvalidInputException;
import com.example.wdmsystem.exception.NotFoundException;
import com.example.wdmsystem.reservation.system.IReservationRepository;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.wdmsystem.reservation.system.Reservation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service

public class CustomerService {
    private final ICustomerRepository customerRepository;
    private final IReservationRepository reservationRepository;

    public CustomerService(ICustomerRepository customerRepository, IReservationRepository reservationRepository) {
        this.customerRepository = customerRepository;
        this.reservationRepository = reservationRepository;
    }

    public Customer createCustomer(CustomerDTO createCustomer) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Customer customer = new Customer();
        customer.setMerchantId(currentUser.getMerchantId());
        customer.setFirstName(createCustomer.firstName());
        customer.setLastName(createCustomer.lastName());
        customer.setPhone(createCustomer.phone());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());

        return customerRepository.save(customer);
    }

    public List<Customer> getCustomers(LocalDateTime createdAtMin, LocalDateTime createdAtMax, Integer limit) {
        if (createdAtMin != null && createdAtMax != null && createdAtMin.isAfter(createdAtMax)) {
            throw new InvalidInputException("createdAtMin must be earlier than createdAtMax.");
        }

        if (limit == null || limit <= 0 || limit > 250) {
            limit = 50;
        }
        PageRequest pageRequest = PageRequest.of(0, limit);
        return customerRepository.findCustomersWithinDateRange(createdAtMin, createdAtMax, pageRequest);
    }

    public Customer getCustomer(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));
    }

    @Transactional
    public void updateCustomer(int id, CustomerDTO request) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isEmpty()) {
            throw new NotFoundException("Customer not found");
        }

        Customer existingCustomer = customer.get();

        if (request.firstName() != null) {
            if (request.firstName().length() > 30) {
                throw new InvalidInputException("First name must be less than 30 characters");
            }
            existingCustomer.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            if (request.lastName().length() > 30) {
                throw new InvalidInputException("Last name must be less than 30 characters");
            }
            existingCustomer.setLastName(request.lastName());
        }
        if (request.phone() != null) {
            existingCustomer.setPhone(request.phone());
        }

        existingCustomer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(existingCustomer);
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

        LocalDateTime upcomingDate = null;
        if (upcoming) {
            upcomingDate = LocalDateTime.now();
        }
        PageRequest pageRequest = PageRequest.of(0, limit);

        return reservationRepository.findReservationByCustomerId(customerId, upcomingDate, pageRequest);
    }
}
