package wdmsystem.service;

import wdmsystem.category.Category;
import wdmsystem.discount.Discount;
import wdmsystem.reservation.Reservation;
import wdmsystem.tax.Tax;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import wdmsystem.merchant.Merchant;

import java.time.LocalDateTime;
import java.util.List;

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
  
    public Service(Merchant merchant, String title, Category category, double price, Discount discount, Tax tax, int durationMins, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.merchant = merchant;
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


