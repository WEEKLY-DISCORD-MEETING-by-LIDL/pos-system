package com.example.wdmsystem.category.system;

import com.example.wdmsystem.product.system.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;
    public int merchantId;
    public String title;

    @OneToMany(mappedBy = "category")
    List<Product> products;

    public Category(Integer id, int merchantId, String title) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
    }

    //@Entity required constructor
    public Category() {
    }
}
