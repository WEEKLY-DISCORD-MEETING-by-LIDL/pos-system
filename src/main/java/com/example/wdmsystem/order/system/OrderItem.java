package com.example.wdmsystem.order.system;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public final class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public int orderId;
    public int productVariantId;
    public int quantity;
    public double price;

    public OrderItem(int id, int orderId, int productVariantId, int quantity, double price) {
        this.id = id;
        this.orderId = orderId;
        this.productVariantId = productVariantId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItem() {

    }
}
