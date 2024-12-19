package wdmsystem.payment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
public class PaymentController {

    private PaymentService _paymentService;

    public PaymentController(PaymentService paymentService) {
        _paymentService = paymentService;
    }

    @PostMapping("/pay")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO payment) {
        log.info("Received request to create payment with details: {}", payment);
        _paymentService.createPayment(payment);
        log.info("Payment created successfully:");
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
