package wdmsystem.order.discount;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderDiscountRepository extends JpaRepository<OrderDiscount, Integer> {
    List<OrderDiscount> findByMerchantId(int merchantId, Pageable pageable);
}
