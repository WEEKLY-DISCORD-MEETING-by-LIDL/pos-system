package com.example.wdmsystem.tax.system;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import com.example.wdmsystem.merchant.system.Merchant;

@Entity
@Getter
@Setter
public class Tax {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant merchant;

    public String title;
    public double percentage;
    public Date createdAt;
    public Date updatedAt;

    public Tax(Integer id, Merchant merchant, String title, double percentage, Date createdAt) {
        this.id = id;
        this.merchant = merchant;
        this.title = title;
        this.percentage = percentage;
        this.createdAt = createdAt;
        this.updatedAt = null;
    }

    //@Entity required constructor
    public Tax() {

    }
}
