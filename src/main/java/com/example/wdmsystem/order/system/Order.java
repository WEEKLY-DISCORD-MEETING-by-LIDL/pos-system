package com.example.wdmsystem.order.system;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Getter
@Setter
public final class Order {
    public int id;
    public int merchantId;
    @Nullable
    public Integer orderDiscountId;
    public OrderStatus status;
    public double totalAmount;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Order(int id, int merchantId, int orderDiscountId, OrderStatus status, double totalAmount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.merchantId = merchantId;
        this.orderDiscountId = orderDiscountId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
