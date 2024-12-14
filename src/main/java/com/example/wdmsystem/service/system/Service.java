package com.example.wdmsystem.service.system;

import com.example.wdmsystem.category.system.Category;
import com.example.wdmsystem.discount.system.Discount;
import com.example.wdmsystem.reservation.system.Reservation;
import com.example.wdmsystem.tax.system.Tax;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public final class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public int merchantId;
    public String title;

    @ManyToOne
    @JoinColumn(name = "category_id")
    public Category category;

    public double price;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    public Discount discount;

    @ManyToOne
    @JoinColumn(name = "tax_id")
    public Tax tax;

    public int durationMins;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Reservation> reservations;

    public Service(int merchantId, String title, Category category, double price, Discount discount,
                   Tax tax, int durationMins, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.merchantId = merchantId;
        this.title = title;
        this.category = category;
        this.price = price;
        this.discount = discount;
        this.tax = tax;
        this.durationMins = durationMins;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //@Entity required constructor
    public Service() {

    }
}


