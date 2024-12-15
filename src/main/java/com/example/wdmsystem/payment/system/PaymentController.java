package com.example.wdmsystem.payment.system;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private PaymentService _paymentService;

    public PaymentController(PaymentService paymentService) {
        _paymentService = paymentService;
    }

    @PostMapping("/pay")
    ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO payment) {
        _paymentService.createPayment(payment);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
