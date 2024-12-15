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

import com.example.wdmsystem.merchant.system.Merchant;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;   
    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant merchant;  
  
    public String title;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public Category category;

    public double price;
    public Integer discountId; //make this work with discount model

    @ManyToOne
    @JoinColumn(name = "tax_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public Tax tax;
    public float weight;
    public String weightUnit;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<ProductVariant> variants;

    public Product(Integer id, Merchant merchant, String title, int categoryId, double price, int discountId, int taxId, float weight, String weightUnit) {
        this.id = id;
        this.merchant = merchant;
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
