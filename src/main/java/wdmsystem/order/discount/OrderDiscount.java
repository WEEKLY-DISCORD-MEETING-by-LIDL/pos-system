package wdmsystem.order.discount;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import wdmsystem.merchant.Merchant;
import wdmsystem.order.Order;

@Entity
@Getter
@Setter
//Discount for a whole order
public final class OrderDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant merchant;

    public String title;
    public double percentage;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "orderDiscount", cascade = CascadeType.REFRESH)
    public List<Order> discountedOrders;

    public OrderDiscount (Integer id, Merchant merchant, String title, double percentage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.merchant = merchant;
        this.title = title;
        this.percentage = percentage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //@Entity required constructor
    public OrderDiscount() {

    }
}