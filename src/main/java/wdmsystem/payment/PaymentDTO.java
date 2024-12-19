package wdmsystem.payment;

import java.math.BigDecimal;

public record PaymentDTO(Integer id, BigDecimal tipAmount, BigDecimal totalAmount, PaymentMethod method, Integer orderId) {
}
