package com.example.wdmsystem.product.system;

import com.example.wdmsystem.category.system.Category;
import com.example.wdmsystem.tax.system.Tax;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public Integer merchantId;
    public String title;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public Category category;


    public double price;
    public Integer discountId;

    @ManyToOne
    @JoinColumn(name = "tax_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public Tax tax;

    public float weight;
    public String weightUnit;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    public List<ProductVariant> variants;

    public Product(Integer id, Integer merchantId, String title, Category category, double price, Integer discountId, Tax tax, float weight, String weightUnit) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
        this.category = category;
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
