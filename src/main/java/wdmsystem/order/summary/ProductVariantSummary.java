package wdmsystem.order.summary;

import java.math.BigDecimal;

public record ProductVariantSummary(
        String title,
        BigDecimal additionalPrice
) {
}
