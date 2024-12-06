package com.example.wdmsystem.product.system;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public int productId;
    public String title;
    public double additionalPrice;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public ProductVariant(Integer id, int productId, String title, double additionalPrice, LocalDateTime createdAt) {
        this.id = id;
        this.productId = productId;
        this.title = title;
        this.additionalPrice = additionalPrice;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    //@Entity required constructor
    public ProductVariant() {

    }
}
