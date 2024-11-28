package order.system;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class OrderItem {
    public int id;
    public int orderId;
    public int productVariantId;
    public int quantity;
    public double price;

    public OrderItem(int id, int orderId, int productVariantId, int quantity, double price) {
        this.id = id;
        this.orderId = orderId;
        this.productVariantId = productVariantId;
        this.quantity = quantity;
        this.price = price;
    }
}
