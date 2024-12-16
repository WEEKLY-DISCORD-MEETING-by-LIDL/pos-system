package wdmsystem.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IServiceRepository extends JpaRepository<Service, Integer> {
    @Query("SELECT s FROM Service s WHERE (:categoryId IS NULL OR s.category.id = :categoryId)")
    List<Service> findServicesByCategoryId(
            @Param("categoryId") Integer categoryId,
            Pageable pageable
    );

    @Query("SELECT s FROM Service s WHERE ((:categoryId IS NULL OR s.category.id = :categoryId) AND s.merchant.id = :merchantId)")
    List<Service> findServicesByCategoryIdAndMerchantId(
            @Param("categoryId") Integer categoryId,
            @Param("merchantId") Integer merchantId,
            Pageable pageable
    );
}
