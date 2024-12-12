package com.example.wdmsystem.product.system;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Integer> {
    List<Product> getProductsByCategoryIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(int categoryId, LocalDateTime createdAtIsGreaterThan, LocalDateTime createdAtIsLessThan, Limit limit);
}
