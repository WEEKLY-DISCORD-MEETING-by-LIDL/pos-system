package wdmsystem.order.discount;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
public class OrderDiscountController {

    private final OrderDiscountService _orderDiscountService;

    public OrderDiscountController(OrderDiscountService orderDiscountService) {
        this._orderDiscountService = orderDiscountService;
    }

    @PostMapping("/orderDiscounts")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<OrderDiscountDTO> createOrderDiscount(@RequestBody OrderDiscountDTO discount) {
        log.info("Received request to create order discount: {}", discount);
        OrderDiscountDTO newDiscount = _orderDiscountService.createOrderDiscount(discount);
        log.info("Created new discount with the following content: {}", newDiscount);
        return new ResponseEntity<>(newDiscount, HttpStatus.CREATED);
    }

    @GetMapping("/orderDiscounts")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OWNER') or hasRole('REGULAR')")
    ResponseEntity<List<OrderDiscountDTO>> getOrderDiscounts( @RequestParam(required = false) Integer limit) {
        log.info("Fetching up to {} amount of discounts", limit);
        List<OrderDiscountDTO> discounts = _orderDiscountService.getOrderDiscounts(limit);
        log.info("Fetched up to {} amount of discounts", discounts);
        return new ResponseEntity<>(discounts, HttpStatus.OK);
    }

    @GetMapping("/orderDiscounts/{orderDiscountId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderDiscountService.isOwnedByCurrentUser(#orderDiscountId))")
    ResponseEntity<OrderDiscountDTO> getOrderDiscount(@PathVariable int orderDiscountId) {
        log.info("Fetching order discount with ID: {}", orderDiscountId);
        OrderDiscountDTO discount = _orderDiscountService.getOrderDiscount(orderDiscountId);
        log.info("Fetched order discount with the following content {}", discount);
        return new ResponseEntity<>(discount, HttpStatus.OK);
    }

    @PutMapping("/orderDiscounts/{orderDiscountId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderDiscountService.isOwnedByCurrentUser(#orderDiscountId))")
    ResponseEntity<OrderDiscountDTO> updateOrderDiscount(@PathVariable int orderDiscountId, @RequestBody OrderDiscountDTO discount) {
        log.info("Received request to update order discount with id {} with the following content: {}", orderDiscountId, discount);
        OrderDiscountDTO updatedDiscount = _orderDiscountService.updateOrderDiscount(orderDiscountId, discount);
        log.info("Updated order discount with the following content: {}", updatedDiscount);
        return new ResponseEntity<>(updatedDiscount, HttpStatus.OK);
    }

    @DeleteMapping("/orderDiscounts/{orderDiscountId}")
    @PreAuthorize("hasRole('ADMIN') or ((hasRole('OWNER') or hasRole('REGULAR')) and @orderDiscountService.isOwnedByCurrentUser(#orderDiscountId))")
    ResponseEntity<OrderDiscountDTO> deleteOrderDiscount(@PathVariable int orderDiscountId) {
        log.info("Received request to delete order discount with ID: {}", orderDiscountId);
        _orderDiscountService.deleteOrderDiscount(orderDiscountId);
        log.info("Deleted order discount with ID: {}", orderDiscountId);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
}
