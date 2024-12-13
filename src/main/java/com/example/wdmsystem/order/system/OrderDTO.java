package com.example.wdmsystem.order.system;

import java.time.LocalDateTime;

public record OrderDTO(Integer id, Integer merchantId, Integer orderDiscountId, OrderStatus status) {

}
