package wdmsystem.category.system;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByMerchantId(int merchantId);
}
