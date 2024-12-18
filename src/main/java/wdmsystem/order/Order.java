package wdmsystem.order;

import wdmsystem.order.discount.OrderDiscount;
import wdmsystem.payment.Payment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import wdmsystem.merchant.Merchant;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public final class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    public Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "order_discount_id")
    public OrderDiscount orderDiscount;

    @Enumerated(EnumType.STRING)
    public OrderStatus status;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    public List<OrderItem> orderItems;

    @OneToMany(mappedBy = "order")
    public List<Payment> payments;

    public Order(Integer id, Merchant merchant, OrderDiscount orderDiscount, OrderStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.merchant = merchant;
        this.orderDiscount = orderDiscount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //@Entity required constructor
    public Order() {

    }
}
