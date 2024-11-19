package product.system;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductVariant {

    public int id;
    public int productId;
    public String title;
    public double additionalPrice;
    public int quantity;
    public Date createdAt;
    public Date updatedAt;

    public ProductVariant(int id, int productId, String title, double additionalPrice, int quantity, Date createdAt) {
        this.id = id;
        this.productId = productId;
        this.title = title;
        this.additionalPrice = additionalPrice;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = null;
    }

}
