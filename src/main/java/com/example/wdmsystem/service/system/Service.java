package com.example.wdmsystem.service.system;

import com.example.wdmsystem.reservation.system.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

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
    @Nullable public int categoryId;
    public double price;
    @Nullable public int discountId;
    @Nullable public int taxId;
    public int durationMins;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Reservation> reservations;

    public Service(Integer id, int merchantId, String title, int categoryId, double price, int discountId, int taxId, int durationMins, LocalDateTime createdAt) {
        this.id = id;
        this.merchantId = merchantId;
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


