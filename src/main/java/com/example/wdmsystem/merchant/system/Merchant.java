package com.example.wdmsystem.merchant.system;

import java.time.LocalDateTime;

public class Merchant {

    public int id;
    public String name;
    public double vat;
    public Address address;
    public String email;
    public String phone;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Merchant(int id, String name, double vat, Address address, String email, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.vat = vat;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = null;
    }


}
