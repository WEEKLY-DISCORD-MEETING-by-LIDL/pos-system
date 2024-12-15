package wdmsystem.order;

public record OrderItemDTO(Integer id, Integer orderId, Integer productVariantId, int quantity) {
}
