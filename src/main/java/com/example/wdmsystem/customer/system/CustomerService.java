package com.example.wdmsystem.customer.system;


import org.springframework.stereotype.Service;
import com.example.wdmsystem.reservation.system.Reservation;

import java.util.Date;
import java.util.List;

@Service
public final class CustomerService {
    //TODO:Add database interaction logic
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(CustomerDTO createCustomer) {
        return null;
    }

    public List<Customer> getCustomers(Date createdAtMin, Date createdAtMax, int limit) {
        return null;
    }

    public Customer getCustomer(int id) {
        return null;
    }

    public void updateCustomer(int id, CustomerDTO request) {
    }

    public void deleteCustomer(int id) {
    }

    public List<Reservation> getCustomerReservations(int customerId, boolean upcoming, int limit) {
        return null;
    }
}
