package wdmsystem.order.summary;

public record OrderItemSummary(
        int quantity,
        double price,
        ProductSummary product,
        ProductVariantSummary variant
) {
}
