package service.system;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.Date;

@Getter
@Setter
public final class Service {
    public int id;
    public String title;
    @Nullable public int categoryId;
    public Price price;
    @Nullable public int discountId;
    @Nullable public int taxId;
    public int durationMins;
    public Date createdAt;
    public Date updatedAt;

    public Service(int id, String title, int categoryId, Price price, int discountId, int taxId, int durationMins, Date createdAt) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.price = price;
        this.discountId = discountId;
        this.taxId = taxId;
        this.durationMins = durationMins;
        this.createdAt = createdAt;
        this.updatedAt = null;
    }

}


