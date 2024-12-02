package com.example.wdmsystem.merchant.system;

import java.time.LocalDateTime;

public class merchant {

    public int id;
    public String name;
    public double vat;
    public address address;
    public String email;
    public String phone;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public merchant(int id, String name, double vat, address address, String email, String phone, LocalDateTime createdAt) {
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
