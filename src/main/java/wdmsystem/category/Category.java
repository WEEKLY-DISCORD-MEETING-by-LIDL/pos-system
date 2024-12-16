package wdmsystem.category;

import wdmsystem.product.Product;
import wdmsystem.merchant.Merchant;
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

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant merchant;

    public String title;

    @OneToMany(mappedBy = "category")
    List<Product> products;

    public Category(Integer id, Merchant merchant, String title) {
        this.id = id;
        this.merchant = merchant;
        this.title = title;
    }

    //@Entity required constructor
    public Category() {
    }
}
