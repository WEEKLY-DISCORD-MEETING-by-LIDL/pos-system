package com.example.wdmsystem.customer.system;

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
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public int merchantId;
    //TODO: Add string restrictions of max length 30
    public String firstName;
    public String lastName;
    public String phone;
    /// Not defined in document, but present in API contract
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public Customer(int id, int merchantId, String firstName, String lastName, String phone, LocalDateTime createdAt) {
        this.id = id;
        this.merchantId = merchantId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = null;
    }

    //@Entity required constructor
    public Customer() {

    }
}
