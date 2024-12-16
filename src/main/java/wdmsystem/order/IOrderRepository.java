package wdmsystem.order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderRepository extends JpaRepository<Order, Integer> {

    List<Order> getOrdersByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(
            LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Pageable pageable);

    List<Order> getOrdersByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndMerchantId(
            LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Integer merchantId, Pageable pageable);

    List<Order> getOrdersByStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(
            OrderStatus status, LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Pageable pageable);

    List<Order> getOrdersByStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndMerchantId(
            OrderStatus status, LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Integer merchantId, Pageable pageable);
}
