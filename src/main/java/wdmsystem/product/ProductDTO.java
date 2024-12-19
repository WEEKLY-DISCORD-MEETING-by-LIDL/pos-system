package wdmsystem.product;

import java.math.BigDecimal;

public record ProductDTO(Integer id, Integer merchantId, String title, Integer categoryId, BigDecimal price, Integer discountId, Integer taxId, float weight, String weightUnit) {
}