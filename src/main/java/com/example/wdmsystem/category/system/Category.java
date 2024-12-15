package com.example.wdmsystem.category.system;

import com.example.wdmsystem.merchant.system.Merchant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant merchant;

    public String title;

    public Category(Integer id, Merchant merchant, String title) {
        this.id = id;
        this.merchant = merchant;
        this.title = title;
    }

    //@Entity required constructor
    public Category() {
    }
}
