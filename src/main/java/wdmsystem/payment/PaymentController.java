package wdmsystem.payment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO payment) {
        _paymentService.createPayment(payment);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
