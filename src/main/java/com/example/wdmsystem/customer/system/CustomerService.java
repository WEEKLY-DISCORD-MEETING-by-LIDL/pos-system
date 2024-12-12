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
        if (createCustomer.firstName().length() > 30) {
            throw new InvalidInputException("First name must be 30 characters or less");
        }
        if (createCustomer.lastName().length() > 30) {
            throw new InvalidInputException("Last name must be 30 characters or less");
        }

        Customer customer = new Customer();
        customer.setMerchantId(1); //what to set
        customer.setFirstName(createCustomer.firstName());
        customer.setLastName(createCustomer.lastName());
        customer.setPhone(createCustomer.phone());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(null);

        return customerRepository.save(customer);
    }

    public List<Customer> getCustomers(LocalDateTime createdAtMin, LocalDateTime createdAtMax, int limit) {
        if (createdAtMin != null && createdAtMax != null && createdAtMin.isAfter(createdAtMax)) {
            throw new InvalidInputException("createdAtMin must be earlier than createdAtMax.");
        }
//        if (limit <= 0) {
//            throw new InvalidInputException("limit must be greater than 0. Current limit: " + limit);
//        }
        if (limit < 0 || limit > 250) {
            limit = 50;
        }
        PageRequest pageRequest = PageRequest.of(0, limit);
        return customerRepository.findCustomersWithinDateRange(createdAtMin, createdAtMax, pageRequest);
    }

    public Customer getCustomer(int id) {
        if (id < 0) {
            throw new InvalidInputException("id must be greater than 0");
        }

        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            return customer.get();
        }
        else {
            throw new NotFoundException("Customer with id " + id + "not found");
        }
    }

    @Transactional
    public void updateCustomer(int id, CustomerDTO request) {
        if (id <= 0) {
            throw new InvalidInputException("CustomerId must be greater than 0. Current is: " + id);
        }

        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isEmpty()) {
            throw new NotFoundException("Customer not found");
        }

        Customer existingCustomer = customer.get();

        if (request.firstName() != null) {
            if (request.firstName().trim().isEmpty()) {
                throw new InvalidInputException("First name cannot be empty or only whitespace");
            }
            if (request.firstName().length() > 30) {
                throw new InvalidInputException("First name must be 30 characters or less");
            }
            existingCustomer.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            if (request.lastName().trim().isEmpty()) {
                throw new InvalidInputException("Last name cannot be empty or whitespace");
            }
            if (request.lastName().length() > 30) {
                throw new InvalidInputException("Last name must be 30 characters or less");
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
        if (id < 0) {
            throw new InvalidInputException("Customer Id must be greater than 0");
        }

        Optional<Customer> customerOptional = customerRepository.findById(id);

        if (customerOptional.isEmpty()) {
            throw new NotFoundException("Customer not found");
        }

        Customer customer = customerOptional.get();
        customerRepository.delete(customer);
    }

    public List<Reservation> getCustomerReservations(int customerId, boolean upcoming, int limit) {
        if (customerId < 0) {
            throw new InvalidInputException("Customer Id must be greater than 0");
        }
//        if (limit <= 0) {
//            throw new InvalidInputException("limit must be greater than 0. Current limit: " + limit);
//        }
        if (limit < 0 || limit > 250) {
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
