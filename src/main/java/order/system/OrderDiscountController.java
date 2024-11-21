package order.system;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public final class OrderDiscountController {

    private final OrderDiscountService _orderDiscountService;

    public OrderDiscountController(OrderDiscountService orderDiscountService) {
        this._orderDiscountService = orderDiscountService;
    }

    @PostMapping("/orderDiscounts")
    ResponseEntity<OrderDiscount> createOrderDiscount(@RequestBody OrderDiscount discount) {
        OrderDiscount newDiscount = _orderDiscountService.createOrderDiscount(discount);
        return new ResponseEntity<>(newDiscount, HttpStatus.CREATED);
    }

    @GetMapping("/orderDiscounts")
    ResponseEntity<List<OrderDiscount>> getFilteredDiscounts(@RequestParam boolean expired, @RequestParam int limit) {
        List<OrderDiscount> discounts = _orderDiscountService.getFilteredDiscounts(expired, limit);
        return new ResponseEntity<>(discounts, HttpStatus.OK);
    }

    @GetMapping("/orderDiscounts/{orderDiscountId}")
    ResponseEntity<OrderDiscount> getOrderDiscount(@PathVariable int orderDiscountId) {
        OrderDiscount discount = _orderDiscountService.getOrderDiscount(orderDiscountId);
        return new ResponseEntity<>(discount, HttpStatus.OK);
    }

    @PutMapping("/orderDiscounts/{orderDiscountId}")
    ResponseEntity<OrderDiscount> updateOrderDiscount(@PathVariable int orderDiscountId, @RequestBody OrderDiscount discount) {
        OrderDiscount updatedDiscount = _orderDiscountService.updateOrderDiscount(orderDiscountId, discount);
        return new ResponseEntity<>(updatedDiscount, HttpStatus.OK);
    }

    @DeleteMapping("/orderDiscounts/{orderDiscountId}")
    ResponseEntity<OrderDiscount> deleteOrderDiscount(@PathVariable int orderDiscountId) {
        HttpStatus status = _orderDiscountService.deleteOrderDiscount(orderDiscountId);
        return new ResponseEntity<>(null, status);
    }
}
