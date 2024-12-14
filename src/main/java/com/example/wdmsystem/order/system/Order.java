package com.example.wdmsystem.order.system;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import com.example.wdmsystem.merchant.system.Merchant;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public final class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "order_discount_id")
    public OrderDiscount orderDiscount;

    @Enumerated(EnumType.STRING)
    public OrderStatus status;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    public Order(Integer id, Merchant merchant, OrderDiscount orderDiscount, OrderStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.merchant = merchant;
        this.orderDiscount = orderDiscount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //@Entity required constructor
    public Order() {

    }
}
