package wdmsystem.tax.system;

import wdmsystem.product.system.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import wdmsystem.merchant.system.Merchant;

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
    public double percentage; //0.0 - 1.0 ~ 0% - 100%
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
  
    @OneToMany(mappedBy = "tax")
    public List<Product> products;

    public Tax(Integer id, Merchant merchant, String title, double percentage) {
        this.id = id;
        this.merchant = merchant;
        this.title = title;
        this.percentage = percentage;
    }

    //@Entity required constructor
    public Tax() {

    }
}
