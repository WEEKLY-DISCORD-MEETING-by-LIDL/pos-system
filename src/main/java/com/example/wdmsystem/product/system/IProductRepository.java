package com.example.wdmsystem.product.system;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Integer> {
    List<Product> getProductsByCategoryIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(int categoryId, LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Limit limit);
    List<Product> getProductsByCategoryIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndMerchantId(int categoryId, LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Limit limit, int merchantId);

    List<Product> getProductsByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Limit limit);
    List<Product> getProductsByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqualAndMerchantId(LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Limit limit, int merchantId);
}
