package wdmsystem.order.discount;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderDiscountController {

    private final OrderDiscountService _orderDiscountService;

    public OrderDiscountController(OrderDiscountService orderDiscountService) {
        this._orderDiscountService = orderDiscountService;
    }

    @PostMapping("/orderDiscounts")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<OrderDiscountDTO> createOrderDiscount(@RequestBody OrderDiscountDTO discount) {
        OrderDiscountDTO newDiscount = _orderDiscountService.createOrderDiscount(discount);
        return new ResponseEntity<>(newDiscount, HttpStatus.CREATED);
    }

    @GetMapping("/orderDiscounts")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<OrderDiscountDTO>> getOrderDiscounts( @RequestParam(required = false) Integer limit) {
        List<OrderDiscountDTO> discounts = _orderDiscountService.getOrderDiscounts(limit);
        return new ResponseEntity<>(discounts, HttpStatus.OK);
    }

    @GetMapping("/orderDiscounts/{orderDiscountId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderDiscountService.isOwnedByCurrentUser(#orderDiscountId))")
    ResponseEntity<OrderDiscountDTO> getOrderDiscount(@PathVariable int orderDiscountId) {
        OrderDiscountDTO discount = _orderDiscountService.getOrderDiscount(orderDiscountId);
        return new ResponseEntity<>(discount, HttpStatus.OK);
    }

    @PutMapping("/orderDiscounts/{orderDiscountId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderDiscountService.isOwnedByCurrentUser(#orderDiscountId))")
    ResponseEntity<OrderDiscountDTO> updateOrderDiscount(@PathVariable int orderDiscountId, @RequestBody OrderDiscountDTO discount) {
        OrderDiscountDTO updatedDiscount = _orderDiscountService.updateOrderDiscount(orderDiscountId, discount);
        return new ResponseEntity<>(updatedDiscount, HttpStatus.OK);
    }

    @DeleteMapping("/orderDiscounts/{orderDiscountId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderDiscountService.isOwnedByCurrentUser(#orderDiscountId))")
    ResponseEntity<OrderDiscountDTO> deleteOrderDiscount(@PathVariable int orderDiscountId) {
        _orderDiscountService.deleteOrderDiscount(orderDiscountId);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
}
