package com.example.wdmsystem.order.system;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public final class OrderDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public int merchantId;
    public String title;
    public double percentage;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public OrderDiscount (Integer id, int merchantId, String title, double percentage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
        this.percentage = percentage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //@Entity required constructor
    public OrderDiscount() {

    }
}
