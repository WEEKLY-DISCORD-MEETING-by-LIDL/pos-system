package com.example.wdmsystem.product.system;

import com.example.wdmsystem.tax.system.Tax;
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
    public int merchantId;
    public String title;
    @Nullable public int categoryId;
    public double price;
    @Nullable public int discountId;

    @ManyToOne
    @JoinColumn(name = "tax_id")
    public Tax tax;
    public float weight;
    public String weightUnit;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product")
    public List<ProductVariant> variants;

    public Product(Integer id, int merchantId, String title, int categoryId, double price, int discountId, Tax tax, float weight, String weightUnit) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
        this.price = price;
        this.discountId = discountId;
        this.tax = tax;
        this.weight = weight;
        this.weightUnit = weightUnit;
    }

    //@Entity required constructor
    public Product() {

    }
}
