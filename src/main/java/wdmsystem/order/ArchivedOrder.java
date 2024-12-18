package wdmsystem.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import wdmsystem.merchant.Merchant;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class ArchivedOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    Merchant merchant;

    @Column(length = 4096)
    public String orderSummaryJson;

    public LocalDateTime archivedDate;

    public ArchivedOrder(Integer id, Merchant merchant, String orderSummaryJson, LocalDateTime archivedDate) {
        this.id = id;
        this.merchant = merchant;
        this.orderSummaryJson = orderSummaryJson;
        this.archivedDate = archivedDate;
    }

    public ArchivedOrder() {}
}
