package com.example.wdmsystem.customer.system;


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
        PageRequest pageRequest = PageRequest.of(0, limit);
        return customerRepository.findCustomersWithinDateRange(createdAtMin, createdAtMax, pageRequest);
    }

    public Customer getCustomer(int id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElse(null);
    }

    @Transactional
    public void updateCustomer(int id, CustomerDTO request) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isEmpty()) {
            throw new NotFoundException("Customer not found");
        }

        Customer existingCustomer = customer.get();

        if (request.firstName() != null) {
            existingCustomer.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            existingCustomer.setLastName(request.lastName());
        }
        if (request.phone() != null) {
            existingCustomer.setPhone(request.phone());
        }

        existingCustomer.setUpdatedAt(LocalDateTime.now());
        customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }

    public List<Reservation> getCustomerReservations(int customerId, boolean upcoming, int limit) {
        LocalDateTime upcomingDate = null;
        if (upcoming) {
            upcomingDate = LocalDateTime.now();
        }
        PageRequest pageRequest = PageRequest.of(0, limit);

        return reservationRepository.findReservationByCustomerId(customerId, upcomingDate, pageRequest);
    }
}
