package com.example.wdmsystem.tax.system;

import com.example.wdmsystem.product.system.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Tax {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public int merchantId;
    public String title;
    public double percentage; //0.0 - 1.0 ~ 0% - 100%
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "tax")
    public List<Product> products;

    public Tax(Integer id, int merchantId, String title, double percentage) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
        this.percentage = percentage;
    }

    //@Entity required constructor
    public Tax() {

    }
}
