package wdmsystem.payment;

import wdmsystem.order.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import wdmsystem.reservation.Reservation;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public double tipAmount;
    public double totalAmount;
    @Enumerated(EnumType.STRING)
    public PaymentMethod method;

    @ManyToOne@JoinColumn(name = "reservation_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public Reservation reservation;
    //reservation
    @ManyToOne
    @JoinColumn(name = "order_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    public Order order;

    public LocalDateTime createdAt;

    public Payment(Integer id, double tipAmount, double totalAmount, PaymentMethod method, Order order) {
        this.id = id;
        this.tipAmount = tipAmount;
        this.totalAmount = totalAmount;
        this.method = method;
        this.order = order;
    }

    public Payment() {

    }
}
