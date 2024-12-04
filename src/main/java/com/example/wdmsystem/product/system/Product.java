package com.example.wdmsystem.product.system;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.lang.Nullable;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public int merchantId;
    public String title;
    @Nullable public int categoryId;
    public double price;
    @Nullable public int discountId;
    @Nullable public int taxId;
    public float weight;
    public String weightUnit;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Product(Integer id, int merchantId, String title, int categoryId, double price, int discountId, int taxId, float weight, String weightUnit, LocalDateTime createdAt) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
        this.price = price;
        this.discountId = discountId;
        this.taxId = taxId;
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.createdAt = createdAt;
        this.updatedAt = null;
    }

    //@Entity required constructor
    public Product() {

    }
}
