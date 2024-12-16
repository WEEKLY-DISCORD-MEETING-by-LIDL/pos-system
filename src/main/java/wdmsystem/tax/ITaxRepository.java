package wdmsystem.tax;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITaxRepository extends JpaRepository<Tax, Integer> {
    List<Tax> getTaxesByMerchantId(int merchantId, Limit limit);
}
