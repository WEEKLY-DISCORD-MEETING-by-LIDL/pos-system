package wdmsystem.order;

import wdmsystem.exception.NotFoundException;
import wdmsystem.product.ProductVariant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    public Order order;

    @ManyToOne
    @JoinColumn(name = "variant_id", referencedColumnName = "id")
    public ProductVariant productVariant;

    public int quantity;

    public OrderItem(int id, Order order, ProductVariant productVariant, int quantity) {
        this.id = id;
        this.order = order;
        this.productVariant = productVariant;
        this.quantity = quantity;
    }

    public OrderItem() {

    }

    public double getTotalPrice() {
        double totalPrice;
        if (productVariant != null && productVariant.getProduct() != null) {
            totalPrice = (productVariant.getProduct().getPrice() + productVariant.getAdditionalPrice()) * quantity;
            if(productVariant.getProduct().getDiscount() != null && productVariant.getProduct().getDiscount().getExpiresOn().isAfter(LocalDateTime.now())) {
                totalPrice = totalPrice * (1 - productVariant.getProduct().getDiscount().percentage);
            }
            if(productVariant.getProduct().getTax() != null) {
                totalPrice = totalPrice * (productVariant.getProduct().getTax().percentage + 1);
            }
            return totalPrice;
        }
        throw new NotFoundException("Product or ProductVariant was not found.");
    }

}
