package wdmsystem.order.summary;

import java.math.BigDecimal;

public record OrderItemSummary(
        int quantity,
        BigDecimal price,
        ProductSummary product,
        ProductVariantSummary variant
) {
}
