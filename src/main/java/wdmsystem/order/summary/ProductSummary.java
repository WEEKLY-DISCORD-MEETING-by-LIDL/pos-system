package wdmsystem.order.summary;

public record ProductSummary(
        String title,
        double price,
        TaxSummary tax,
        float weight,
        String weightUnit,
        ProductDiscountSummary discount
) {}
