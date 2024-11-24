package product.system;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import org.springframework.lang.Nullable;

@Getter
@Setter
public class Product {

    public int id;
    public int merchantId;
    public String title;
    @Nullable public int categoryId;
    public Price price;
    @Nullable public int discountId;
    @Nullable public int taxId;
    public float weight;
    public String weightUnit;
    public Date createdAt;
    public Date updatedAt;

    public Product(int id, int merchantId, String title, int categoryId, Price price, int discountId, int taxId, float weight, String weightUnit, Date createdAt) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
        this.price = price;
        this.discountId = discountId;
        this.taxId = taxId;
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.createdAt = createdAt;
        this.updatedAt = null;
    }

}
