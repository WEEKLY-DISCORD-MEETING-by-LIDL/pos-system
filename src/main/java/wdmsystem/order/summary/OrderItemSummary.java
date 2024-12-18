package wdmsystem.order.summary;

public record OrderItemSummary(
        int quantity,
        ProductSummary product,
        ProductVariantSummary variant
) {
}
