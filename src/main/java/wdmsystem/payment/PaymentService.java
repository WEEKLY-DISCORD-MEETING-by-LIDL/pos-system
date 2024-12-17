package wdmsystem.payment;

import wdmsystem.exception.InvalidInputException;
import wdmsystem.order.IOrderRepository;
import wdmsystem.utility.DTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {

    private final IPaymentRepository paymentRepository;
    private final IOrderRepository orderRepository;
    private final DTOMapper dtoMapper;

    public void createPayment(PaymentDTO paymentDTO) {

        if(paymentDTO.tipAmount() < 0) throw new InvalidInputException("Tip amount must be greater than or equal to 0");
        if(paymentDTO.totalAmount() <= 0) throw new InvalidInputException("Total amount must be greater than 0");

        // well change when reservations work

        Payment payment = dtoMapper.Payment_DTOToModel(paymentDTO);
        paymentRepository.save(payment);
    }
}