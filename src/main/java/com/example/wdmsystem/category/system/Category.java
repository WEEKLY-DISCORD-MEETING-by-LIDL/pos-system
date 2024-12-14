package com.example.wdmsystem.category.system;

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

    public Integer merchantId;

    public String title;

    public Category(Integer id, Integer merchantId, String title) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
    }

    //@Entity required constructor
    public Category() {
    }
}
