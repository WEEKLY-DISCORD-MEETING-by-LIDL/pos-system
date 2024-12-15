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

    //new method
    @GetMapping("/orders")
    ResponseEntity<List<OrderDTO>> getOrders(@RequestParam(required = false) OrderStatus status,
                                             @RequestParam(required = false) String createdAtMin,
                                             @RequestParam(required = false) String createdAtMax,
                                             @RequestParam(required = false) Integer limit) {
        List<OrderDTO> orders = _orderService.getOrders(status, createdAtMin, createdAtMax, limit);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    ResponseEntity<OrderDTO> getOrder(@PathVariable int orderId) {
        OrderDTO order = _orderService.getOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/orders/{orderId}")
    ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable int orderId,
                                            @RequestParam OrderStatus status) {
        OrderDTO order = _orderService.updateOrderStatus(orderId, status);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/orders/{orderId}")
    ResponseEntity<Order> deleteOrder(@PathVariable int orderId) {
        _orderService.deleteOrder(orderId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/orders/{orderId}/cancel")
    ResponseEntity<OrderDTO> cancelOrder(@PathVariable int orderId) {
        OrderDTO order = _orderService.cancelOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PatchMapping("/orders/{orderId}/discount")
    ResponseEntity<OrderDTO> applyDiscountToOrder(@PathVariable int orderId, @RequestParam int discountId) {
        OrderDTO order = _orderService.applyDiscountToOrder(orderId, discountId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}/price")
    public ResponseEntity<Double> getPrice(@PathVariable int orderId) {
        double price = _orderService.getPrice(orderId);
        return new ResponseEntity<>(price, HttpStatus.OK);
    }

    //new method
    @GetMapping("/orders/{orderId}/items")
    ResponseEntity<List<OrderItemDTO>> getOrderItems(@PathVariable int orderId) {
        List<OrderItemDTO> orderItems = _orderService.getOrderItems(orderId);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }
}
