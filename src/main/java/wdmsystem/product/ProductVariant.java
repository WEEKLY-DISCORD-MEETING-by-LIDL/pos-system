package wdmsystem.product;

import wdmsystem.order.OrderItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    public BigDecimal additionalPrice;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL)
    public List<OrderItem> orderItems;

    public ProductVariant(Integer id, Product product, String title, BigDecimal additionalPrice) {
        this.id = id;
        this.product = product;
        this.title = title;
        this.additionalPrice = additionalPrice;
    }

    //@Entity required constructor
    public ProductVariant() {

    }
}
