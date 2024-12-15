package wdmsystem.payment;

public record PaymentDTO(Integer id, double tipAmount, double totalAmount, PaymentMethod method, Integer orderId) {
}
