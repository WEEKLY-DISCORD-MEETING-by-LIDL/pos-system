package wdmsystem.product;

import java.math.BigDecimal;

public record ProductVariantDTO(Integer id, Integer productId, String title, BigDecimal additionalPrice) {
}
