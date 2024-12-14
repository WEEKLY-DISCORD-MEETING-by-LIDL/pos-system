package com.example.wdmsystem.product.system;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.lang.Nullable;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Integer merchantId;

    public String title;
    public int categoryId; //nullable
    public double price;
    public int discountId; //nullable
    public int taxId; //nullable
    public float weight;
    public String weightUnit;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product")
    public List<ProductVariant> variants;

    public Product(Integer id, Integer merchantId, String title, int categoryId, double price, int discountId, int taxId, float weight, String weightUnit) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
        this.price = price;
        this.discountId = discountId;
        this.taxId = taxId;
        this.weight = weight;
        this.weightUnit = weightUnit;
    }

    //@Entity required constructor
    public Product() {

    }
}
