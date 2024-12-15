package wdmsystem.customer.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ICustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("SELECT c FROM Customer c WHERE c.createdAt >= :createdAtMin AND c.createdAt <= :createdAtMax")
    List<Customer> findCustomersWithinDateRange(
            @Param("createdAtMin")LocalDateTime createdAtMin,
            @Param("createdAtMax")LocalDateTime createdAtMax,
            Pageable pageable
            );
}
