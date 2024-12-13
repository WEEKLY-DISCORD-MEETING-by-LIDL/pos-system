package com.example.wdmsystem.order.system;

public record OrderItemDTO(Integer id, Integer orderId, Integer productVariantId, int quantity) {
}
