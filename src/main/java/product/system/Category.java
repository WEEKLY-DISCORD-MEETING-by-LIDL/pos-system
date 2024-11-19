package product.system;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category {

    public int id;
    public int merchantId;
    public String title;

    public Category(int id, int merchantId, String title) {
        this.id = id;
        this.merchantId = merchantId;
        this.title = title;
    }
    
}
