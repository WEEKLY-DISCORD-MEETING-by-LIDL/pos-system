package wdmsystem.product.system;

import wdmsystem.order.system.OrderItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    public Product product;

    public String title;
    public double additionalPrice;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL)
    public List<OrderItem> orderItems;

    public ProductVariant(Integer id, Product product, String title, double additionalPrice) {
        this.id = id;
        this.product = product;
        this.title = title;
        this.additionalPrice = additionalPrice;
    }

    //@Entity required constructor
    public ProductVariant() {

    }
}
