package wdmsystem.order.system;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderRepository extends JpaRepository<Order, Integer> {
    List<Order> getOrdersByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Limit limit);

    List<Order> getOrdersByStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(OrderStatus status, LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Limit limit);
}
