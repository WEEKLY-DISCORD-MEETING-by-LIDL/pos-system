package order.system;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.Date;

@Getter
@Setter
public final class Order {
    public int id;
    public int merchantId;
    @Nullable
    public int orderDiscountId;
    public OrderStatus status;
    public Price totalAmount;
    public Date createdAt;
    public Date updatedAt;

    public Order(int id, int merchantId, int orderDiscountId, OrderStatus status, Price totalAmount, Date createdAt, Date updatedAt) {
        this.id = id;
        this.merchantId = merchantId;
        this.orderDiscountId = orderDiscountId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
