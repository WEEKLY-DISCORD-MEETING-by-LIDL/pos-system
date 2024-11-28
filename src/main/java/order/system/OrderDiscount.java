package order.system;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public final class OrderDiscount {
    public int id;
    public int merchantId;
    public String title;
    public double percentage;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public OrderDiscount (int id, int merchantId, String title, double percentage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
        this.percentage = percentage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
