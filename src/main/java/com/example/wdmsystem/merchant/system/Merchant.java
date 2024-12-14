package com.example.wdmsystem.merchant.system;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public String name;
    public Integer vat; // i think this is supposed to be a tax id and not the vat itself
    @Embedded
    public Address address;
    public String email;
    public String phone;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Merchant(Integer id, String name, Integer vat, Address address, String email, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.vat = vat;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = null;
    }

    //@Entity required constructor
    public Merchant() {

    }
}
