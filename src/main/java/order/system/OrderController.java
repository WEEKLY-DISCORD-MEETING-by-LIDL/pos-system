package order.system;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public final class OrderController {
    private final OrderService _orderService;

    public OrderController(OrderService orderService) {
        this._orderService = orderService;
    }

    @PostMapping("/orders")
    ResponseEntity<Order> createOrder(@RequestBody OrderDTO order) {
        Order newOrder = _orderService.createOrder(order);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping("/orders/{orderId}")
    ResponseEntity<Order> getOrder(@PathVariable int orderId) {
        Order order = _orderService.getOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/orders/{orderId}")
    ResponseEntity<Order> updateOrderStatus(@PathVariable int orderId,
                                            @RequestBody OrderStatus status) {
        Order order = _orderService.updateOrderStatus(orderId, status);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/orders/{orderId}")
    ResponseEntity<Order> deleteOrder(@PathVariable int orderId) {
        HttpStatus httpStatus = _orderService.deleteOrder(orderId);
        return new ResponseEntity<>(null, httpStatus);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    ResponseEntity<Order> cancelOrder(@PathVariable int orderId) {
        HttpStatus httpStatus = _orderService.cancelOrder(orderId);
        return new ResponseEntity<>(null, httpStatus);
    }

    @PostMapping("/orders/{orderId}/discount")
    ResponseEntity<Order> applyDiscountToOrder(@PathVariable int orderId) {
        //Doc says to return an object made of orderId and totalAmount so this could be changed later
        Order order = _orderService.applyDiscountToOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
