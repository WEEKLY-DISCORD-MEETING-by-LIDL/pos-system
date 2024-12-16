package wdmsystem.order.discount;

import wdmsystem.auth.CustomUserDetails;
import wdmsystem.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDiscountService {
    private final IOrderDiscountRepository orderDiscountRepository;

    public OrderDiscountService(IOrderDiscountRepository orderDiscountRepository) {
        this.orderDiscountRepository = orderDiscountRepository;
    }

    public OrderDiscount createOrderDiscount(OrderDiscount discount) {
        return null;
    }
    public List<OrderDiscount> getFilteredDiscounts(boolean expired, int limit){
        return null;
    }
    public OrderDiscount getOrderDiscount(int orderDiscountId) {
        return null;
    }

    public OrderDiscount updateOrderDiscount(int orderDiscountId, OrderDiscount discount) {
        return null;
    }

    public HttpStatus deleteOrderDiscount(int orderDiscountId) {
        return null;
    }

    public boolean isOwnedByCurrentUser(int discountId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        OrderDiscount orderDiscount = orderDiscountRepository.findById(discountId).orElseThrow(() ->
                new NotFoundException("Discount with id " + discountId + " not found"));

        return orderDiscount.getMerchant().getId() == currentUser.getMerchantId();
    }
}
