package wdmsystem.order.summary;

import java.math.BigDecimal;

public record ProductSummary(
        String title,
        BigDecimal price,
        TaxSummary tax,
        float weight,
        String weightUnit,
        ProductDiscountSummary discount
) {}
