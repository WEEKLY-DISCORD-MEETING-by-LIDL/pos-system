package com.example.wdmsystem.order.system;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public final class OrderController {
    private final OrderService _orderService;

    public OrderController(OrderService orderService) {
        this._orderService = orderService;
    }

    @PostMapping("/orders")
    ResponseEntity<OrderDTO> createOrder(@RequestParam(required = false) Integer orderDiscountId, @RequestBody List<OrderItemDTO> orderItems) {
        OrderDTO newOrder = _orderService.createOrder(orderDiscountId, orderItems);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @GetMapping("/orders/{orderId}")
    ResponseEntity<Order> getOrder(@PathVariable int orderId) {
        Order order = _orderService.getOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/orders/{orderId}")
    ResponseEntity<Order> updateOrderStatus(@PathVariable int orderId,
                                            @RequestParam OrderStatus status) {
        Order order = _orderService.updateOrderStatus(orderId, status);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/orders/{orderId}")
    ResponseEntity<Order> deleteOrder(@PathVariable int orderId) {
        _orderService.deleteOrder(orderId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    ResponseEntity<Order> cancelOrder(@PathVariable int orderId) {
        Order order = _orderService.cancelOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PatchMapping("/orders/{orderId}/discount")
    ResponseEntity<Order> applyDiscountToOrder(@PathVariable int orderId, @RequestParam int discountId) {
        Order order = _orderService.applyDiscountToOrder(orderId, discountId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
