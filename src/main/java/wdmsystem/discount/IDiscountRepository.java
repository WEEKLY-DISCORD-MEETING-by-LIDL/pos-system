package wdmsystem.discount;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDiscountRepository extends JpaRepository<Discount, Integer> {
    List<Discount> findByMerchantId(int merchantId);
}
