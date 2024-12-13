package com.example.wdmsystem.order.system;

import com.example.wdmsystem.product.system.ProductVariant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public final class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id", nullable = false)
    public Order order;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "variant_id", referencedColumnName = "id")
    public ProductVariant productVariant;

    public int quantity;
    public double price;

    public OrderItem(int id, Order order, ProductVariant productVariant, int quantity, double price) {
        this.id = id;
        this.order = order;
        this.productVariant = productVariant;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItem() {

    }

    public double getTotalPrice() {
        return this.price * this.quantity;
    }
}
