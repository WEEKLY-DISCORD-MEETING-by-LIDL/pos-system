package com.example.wdmsystem.merchant.system;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import com.example.wdmsystem.customer.system.Customer;
import com.example.wdmsystem.employee.system.Employee;
import com.example.wdmsystem.order.system.Order;
import com.example.wdmsystem.product.system.Product;
import com.example.wdmsystem.service.system.Service;

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

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    List<Order> orders;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    List<Product> products;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    List<Customer> customers;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    List<Employee> employees;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    List<Service> services;

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
