package customer.system;


import org.springframework.stereotype.Service;
import reservation.system.Reservation;

import java.time.LocalDateTime;
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

    public List<Customer> getCustomers(LocalDateTime createdAtMin, LocalDateTime createdAtMax, int limit) {
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
