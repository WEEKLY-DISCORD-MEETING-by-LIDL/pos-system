package wdmsystem.order.summary;

import wdmsystem.order.OrderStatus;

import java.util.List;

public record OrderSummaryDTO(
    OrderDiscountSummary discount,
    OrderStatus status,
    List<OrderItemSummary> items
) {}



