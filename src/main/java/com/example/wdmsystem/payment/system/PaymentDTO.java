package com.example.wdmsystem.payment.system;

public record PaymentDTO(Integer id, double tipAmount, double totalAmount, PaymentMethod method, Integer orderId) {
}
