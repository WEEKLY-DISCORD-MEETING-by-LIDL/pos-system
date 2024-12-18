package wdmsystem.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    List<ProductVariant> getProductVariantsByProductId(int productId);

    @Query("SELECT pv FROM ProductVariant pv JOIN pv.orderItems oi WHERE oi.order.id = :orderId")
    List<ProductVariant> findByOrderId(@Param("orderId") int orderId);
}
