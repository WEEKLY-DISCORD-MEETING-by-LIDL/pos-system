package wdmsystem.order;

import wdmsystem.exception.NotFoundException;
import wdmsystem.product.ProductVariant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

        if (productVariant != null && productVariant.getProduct() != null) {
            if(productVariant.getProduct().getTax() != null) {
                return (productVariant.getProduct().getPrice() + productVariant.getAdditionalPrice()) * (productVariant.getProduct().getTax().percentage + 1) * quantity;
            }
            else {
                return (productVariant.getProduct().getPrice() + productVariant.getAdditionalPrice()) * quantity;
            }

        }
        throw new NotFoundException("Product or ProductVariant was not found.");
    }

}
