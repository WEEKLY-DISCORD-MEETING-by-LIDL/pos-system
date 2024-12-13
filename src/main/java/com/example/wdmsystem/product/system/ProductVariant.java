package com.example.wdmsystem.product.system;

import com.example.wdmsystem.order.system.Order;
import com.example.wdmsystem.order.system.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    public Product product;

    public String title;
    public double additionalPrice;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "productVariant")
    public List<OrderItem> orderItems;

    public ProductVariant(Integer id, Product product, String title, double additionalPrice) {
        this.id = id;
        this.product = product;
        this.title = title;
        this.additionalPrice = additionalPrice;
    }

    //@Entity required constructor
    public ProductVariant() {

    }
}
