package com.example.wdmsystem.tax.system;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Tax {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public int merchantId;
    public String title;
    public double percentage;
    public Date createdAt;
    public Date updatedAt;

    public Tax(Integer id, int merchantId, String title, double percentage, Date createdAt) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
        this.percentage = percentage;
        this.createdAt = createdAt;
        this.updatedAt = null;
    }

    //@Entity required constructor
    public Tax() {

    }
}
