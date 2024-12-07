package com.example.wdmsystem.product.system;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
    public int quantity;
    public Date createdAt;
    public Date updatedAt;

    public ProductVariant(Integer id, int productId, String title, double additionalPrice, int quantity, Date createdAt) {
        this.id = id;
        this.productId = productId;
        this.title = title;
        this.additionalPrice = additionalPrice;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = null;
    }

    //@Entity required constructor
    public ProductVariant() {

    }
}
