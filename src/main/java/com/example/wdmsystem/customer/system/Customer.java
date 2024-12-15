package com.example.wdmsystem.customer.system;

import com.example.wdmsystem.reservation.system.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import com.example.wdmsystem.merchant.system.Merchant;

@Entity
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant merchant;

    //TODO: Add string restrictions of max length 30
    public String firstName;
    public String lastName;
    public String phone;
    /// Not defined in document, but present in API contract
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Reservation> reservations;

    public Customer(Merchant merchant, String firstName, String lastName, String phone, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.merchant = merchant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //@Entity required constructor
    public Customer() {

    }
}
