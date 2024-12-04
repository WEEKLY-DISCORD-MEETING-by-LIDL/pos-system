package com.example.wdmsystem.category.system;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public int merchantId;
    public String title;

    public Category(Long id, int merchantId, String title) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
    }

    //@Entity required constructor
    public Category() {
    }
}
