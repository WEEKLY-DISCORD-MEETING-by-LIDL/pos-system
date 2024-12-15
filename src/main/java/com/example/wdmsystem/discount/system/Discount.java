package com.example.wdmsystem.discount.system;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public int merchantId;
    public String title;
    public double percentage;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Discount(Integer id, int merchantId, String title, double percentage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
        this.percentage = percentage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Discount() {

    }
}
