package customer.system;

import org.springframework.stereotype.Repository;
import reservation.system.Reservation;
import utils.IRepository;

import java.time.LocalDateTime;
import java.util.List;

//TODO: Implement database logic
@Repository
public class CustomerRepository implements IRepository<Customer> {
    public void add(Customer customer) {
    }

    public Customer get(int id) {
        return null;
    }

    public void update(int id, Customer customer) {
    }

    public void delete(int id) {
    }

    public List<Customer> getMultiple(LocalDateTime createdAtMin, LocalDateTime createdAtMax, int limit) {
        return null;
    }

    public List<Reservation> getCustomerReservations(int customerId, boolean upcoming, int limit) {
        return null;
    }
}
