package wdmsystem.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    List<ProductVariant> getProductVariantsByProductId(int productId);
}
