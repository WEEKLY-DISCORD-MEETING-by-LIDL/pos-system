package wdmsystem.product;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Integer> {
    List<Product> getProductsByCategoryIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(int categoryId, LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Limit limit);
    List<Product> getProductsByCategoryIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndMerchantId(int categoryId, LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Limit limit, int merchantId);

    List<Product> getProductsByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Limit limit);
    List<Product> getProductsByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndMerchantId(LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Limit limit, int merchantId);

    @Query("SELECT DISTINCT p FROM Product p " +
            "JOIN p.variants pv " +
            "JOIN pv.orderItems oi " +
            "WHERE oi.order.id = :orderId")
    List<Product> findProductsByOrderId(@Param("orderId") int orderId);
}
