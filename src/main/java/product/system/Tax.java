package product.system;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Tax {

    public int id;
    public int merchantId;
    public String title;
    public double percentage;
    public Date createdAt;
    public Date updatedAt;

    public Tax(int id, int merchantId, String title, double percentage, Date createdAt) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
        this.percentage = percentage;
        this.createdAt = createdAt;
        this.updatedAt = null;
    }

}
