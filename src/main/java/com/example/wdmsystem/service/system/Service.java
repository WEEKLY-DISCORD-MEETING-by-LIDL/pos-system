package com.example.wdmsystem.service.system;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import com.example.wdmsystem.merchant.system.Merchant;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
public final class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant merchant;

    public String title;
    public int categoryId; //nullable
    public double price;
    public int discountId; //nullable
    public int taxId; //nullable
    public int durationMins;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Service(Integer id, Merchant merchant, String title, int categoryId, double price, int discountId, int taxId, int durationMins, LocalDateTime createdAt) {
        this.id = id;
        this.merchant = merchant;
        this.title = title;
        this.categoryId = categoryId;
        this.price = price;
        this.discountId = discountId;
        this.taxId = taxId;
        this.durationMins = durationMins;
        this.createdAt = createdAt;
        this.updatedAt = null;
    }

    //@Entity required constructor
    public Service() {

    }
}


