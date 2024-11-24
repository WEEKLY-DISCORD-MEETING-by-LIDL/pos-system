package order.system;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Price {
    public double amount;
    public String currency;

    public Price(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
}
