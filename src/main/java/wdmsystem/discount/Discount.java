package wdmsystem.discount;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import wdmsystem.merchant.Merchant;
import wdmsystem.product.Product;
import wdmsystem.service.Service;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
//Discount for a specific Product or Service
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant merchant;

    public String title;
    public double percentage;

    public LocalDateTime expiresOn;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Service> services;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Product> products;

    public Discount(Integer id, Merchant merchant, String title, LocalDateTime expiresOn, double percentage) {
        this.id = id;
        this.merchant = merchant;
        this.title = title;
        this.expiresOn = expiresOn;
        this.percentage = percentage;
    }

    public Discount() {

    }
}
