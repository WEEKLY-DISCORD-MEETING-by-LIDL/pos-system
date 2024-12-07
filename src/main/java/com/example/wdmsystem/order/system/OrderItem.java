package com.example.wdmsystem.order.system;

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
    @JoinColumn(name = "order_id", nullable = false)
    public Order order;
    public int productVariantId;
    public int quantity;
    public double price;

    public OrderItem(int id, Order order, int productVariantId, int quantity, double price) {
        this.id = id;
        this.order = order;
        this.productVariantId = productVariantId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItem() {

    }
}
